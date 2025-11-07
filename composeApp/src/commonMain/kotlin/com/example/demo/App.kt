package com.example.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.isSystemInDarkTheme
// --- IMPORTACIONES AÑADIDAS PARA LA LISTA ---
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

// --- IMPORTACIONES CORREGIDAS ---
import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente
import com.example.demo.Dominio.EstadoBoleta
import com.example.demo.Dominio.EstadoCliente
import com.example.demo.Dominio.LecturaConsumo
import com.example.demo.Dominio.TipoTarifa
import com.example.demo.Servicios.BoletaService
import com.example.demo.Persistencia.PersistenciaDatos
import com.example.demo.Servicios.PdfService
import com.example.demo.Servicios.TarifaService

val persistencia = PersistenciaDatos() // Ahora se guardan los Clientes y Boletas aquí
val tarifaService = TarifaService()
val pdfService = PdfService()
val boletaService = BoletaService(
    persistencia, persistencia, persistencia, persistencia, tarifaService
)

// Se define las distintas rutas de la app para la navegación
enum class Pantalla {
    REGISTRAR_CLIENTE,
    REGISTRAR_LECTURA,
    EMITIR_BOLETA,
    VER_CLIENTES,
    VER_BOLETAS
}

//Raíz de la aplicación
@Composable
fun App(
    onOpenPdf: (bytes: ByteArray, filename: String) -> Unit
) {
    val systemIsDark = isSystemInDarkTheme() // Llama a la función composable aquí
    var isDarkMode by remember { mutableStateOf(systemIsDark) } // Se guarda el "valor" en 'remember'
    val lightColors = lightColorScheme()
    val darkColors = darkColorScheme()
    val colorScheme = if (isDarkMode) darkColors else lightColors


    MaterialTheme(
        colorScheme = colorScheme
    ) {
        // Almacena la ruta que se está mostrando actualmente
        var pantallaActual by remember { mutableStateOf(Pantalla.REGISTRAR_CLIENTE) }

        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
                // Switch para el Modo oscuro
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(if (isDarkMode) "Modo Oscuro" else "Modo Claro")
                    Spacer(Modifier.width(8.dp))
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { isDarkMode = it }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    val colorActivo = MaterialTheme.colorScheme.primary
                    val colorInactivo = Color.Gray

                    TextButton(onClick = { pantallaActual = Pantalla.REGISTRAR_CLIENTE }) {
                        Text("Registro", color = if (pantallaActual == Pantalla.REGISTRAR_CLIENTE) colorActivo else colorInactivo)
                    }
                    TextButton(onClick = { pantallaActual = Pantalla.VER_CLIENTES }) {
                        Text("Ver Clientes", color = if (pantallaActual == Pantalla.VER_CLIENTES) colorActivo else colorInactivo)
                    }
                    TextButton(onClick = { pantallaActual = Pantalla.REGISTRAR_LECTURA }) {
                        Text("Lecturas", color = if (pantallaActual == Pantalla.REGISTRAR_LECTURA) colorActivo else colorInactivo)
                    }
                    TextButton(onClick = { pantallaActual = Pantalla.EMITIR_BOLETA }) {
                        Text("Emitir", color = if (pantallaActual == Pantalla.EMITIR_BOLETA) colorActivo else colorInactivo)
                    }
                    TextButton(onClick = { pantallaActual = Pantalla.VER_BOLETAS }) {
                        Text("Ver Boletas", color = if (pantallaActual == Pantalla.VER_BOLETAS) colorActivo else colorInactivo)
                    }
                }
                Divider()


                when (pantallaActual) {

                    Pantalla.REGISTRAR_CLIENTE -> {
                        // Almacena el estado de los campos del formulario
                        var rut by remember { mutableStateOf("") }
                        var nombre by remember { mutableStateOf("") }
                        var email by remember { mutableStateOf("") }
                        var direccion by remember { mutableStateOf("") }
                        var tipoTarifaStr by remember { mutableStateOf("RESIDENCIAL") }
                        var mensaje by remember { mutableStateOf<String?>(null) }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("1. Registrar Cliente", style = MaterialTheme.typography.headlineSmall)
                            Spacer(Modifier.height(16.dp))
                            OutlinedTextField(rut, { rut = it }, label = { Text("RUT Cliente") })
                            OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") })
                            OutlinedTextField(email, { email = it }, label = { Text("Email") })
                            OutlinedTextField(direccion, { direccion = it }, label = { Text("Dirección Facturación") })
                            OutlinedTextField(
                                value = tipoTarifaStr,
                                onValueChange = { tipoTarifaStr = it },
                                label = { Text("Tipo Tarifa (RESIDENCIAL o COMERCIAL)") }
                            )
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = {
                                val tipo = if (tipoTarifaStr.uppercase().startsWith("COMER")) TipoTarifa.COMERCIAL else TipoTarifa.RESIDENCIAL
                                val nuevoCliente = Cliente(
                                    // Se crea el cliente
                                    rut = rut, nombre = nombre, email = email,
                                    direccionFacturacion = direccion,
                                    estado = EstadoCliente.ACTIVO,
                                    tipoTarifa = tipo
                                )
                                try {
                                    // Llama al repositorio para guardar al cliente
                                    persistencia.guardarCliente(nuevoCliente)
                                    mensaje = "¡Cliente $nombre guardado!"
                                    rut = ""; nombre = ""; email = ""; direccion = ""; tipoTarifaStr = "RESIDENCIAL"
                                } catch (e: Exception) {
                                    mensaje = "Error al guardar: ${e.message}"
                                }
                            }) { Text("Guardar Cliente") }
                            // Muestra si se pudo crear el cliente o no
                            Spacer(Modifier.height(16.dp))
                            mensaje?.let {
                                Text(it, color = if (it.startsWith("Error")) Color.Red else Color.Green)
                            }
                        }
                    }

                    Pantalla.VER_CLIENTES -> {
                        // Obtiene los datos recientes del repositorio
                        val clientesGuardados = persistencia.obtenerTodosLosClientes().values.toList()
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Lista de Clientes Guardados", style = MaterialTheme.typography.headlineSmall)
                            Spacer(Modifier.height(16.dp))
                            if (clientesGuardados.isEmpty()) {
                                Text("Aún no hay clientes registrados.")
                            } else {
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(clientesGuardados) { cliente ->
                                        // Muestra cada cliente en tarjetas
                                        Card(modifier = Modifier.fillMaxWidth()) {
                                            // Se muestra la lista de cada cliente
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Text(
                                                    text = "${cliente.nombre} (${cliente.rut})",
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Spacer(Modifier.height(4.dp))
                                                Text(
                                                    text = "Email: ${cliente.email}",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = "Dirección: ${cliente.direccionFacturacion}",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = "Tarifa: ${cliente.tipoTarifa} (${cliente.estado})",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = if(cliente.estado == EstadoCliente.ACTIVO) Color.Green else Color.Red
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Ruta para ver las boletas
                    Pantalla.VER_BOLETAS -> {
                        val boletasGuardadas = persistencia.obtenerTodasLasBoletas()

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Historial de Boletas Emitidas", style = MaterialTheme.typography.headlineSmall)
                            Spacer(Modifier.height(16.dp))

                            if (boletasGuardadas.isEmpty()) {
                                Text("Aún no se han emitido boletas.")
                            } else {
                                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(boletasGuardadas.sortedByDescending { it.anio * 100 + it.mes }) { boleta ->
                                        Card(modifier = Modifier.fillMaxWidth()) {
                                            Column(modifier = Modifier.padding(16.dp)) {
                                                Text(
                                                    text = "Cliente: ${boleta.idCliente}",
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                Text(
                                                    text = "Periodo: ${boleta.mes}/${boleta.anio}",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = "Total: $${boleta.detalle.total}",
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Text(
                                                    text = "Estado: ${boleta.estado}",
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = if(boleta.estado == EstadoBoleta.PAGADA) Color.Green else Color.Gray
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Pantalla.REGISTRAR_LECTURA -> {
                        // Estado del formulario de las lecturas
                        var idMedidor by remember { mutableStateOf("1-9") }
                        var anio by remember { mutableStateOf("2025") }
                        var mes by remember { mutableStateOf("10") }
                        var kwhLeidos by remember { mutableStateOf("") }
                        var mensaje by remember { mutableStateOf<String?>(null) }

                        Column(modifier = Modifier.padding(16.dp)) {
                            // Creación de los campos a rellenar
                            Text("2. Registrar Lectura Mensual", style = MaterialTheme.typography.headlineSmall)
                            Spacer(Modifier.height(16.dp))
                            OutlinedTextField(idMedidor, { idMedidor = it }, label = { Text("ID Medidor (RUT Cliente)") })
                            OutlinedTextField(anio, { anio = it }, label = { Text("Año (ej: 2025)") })
                            OutlinedTextField(mes, { mes = it }, label = { Text("Mes (ej: 10)") })
                            OutlinedTextField(kwhLeidos, { kwhLeidos = it }, label = { Text("KWh Leídos (ej: 1500)") })
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = {
                                try {
                                    // Se crea la lectura de los campos
                                    val nuevaLectura = LecturaConsumo(
                                        idMedidor = idMedidor,
                                        anio = anio.toInt(),
                                        mes = mes.toInt(),
                                        kwhLeidos = kwhLeidos.toDouble()
                                    )
                                    // Guarda la lectura en memoria
                                    persistencia.guardarLectura(nuevaLectura)
                                    // Lectura exitosa
                                    mensaje = "¡Lectura de $kwhLeidos KWh guardada!"
                                    kwhLeidos = ""
                                } catch (e: Exception) {
                                    // No se logró la lectura
                                    mensaje = "Error al guardar la lectura: ${e.message}"
                                }
                            // Muestra mensaje
                            }) { Text("Guardar Lectura") }

                            Spacer(Modifier.height(16.dp))
                            mensaje?.let {
                                Text(it, color = if (it.startsWith("Error")) Color.Red else Color.Green)
                            }
                        }
                    }

                    Pantalla.EMITIR_BOLETA -> {
                        // Creación de los campos a rellenar con ejemplos
                        var rut by remember { mutableStateOf("1-9") }
                        var anio by remember { mutableStateOf("2025") }
                        var mes by remember { mutableStateOf("10") }
                        var boletaResult by remember { mutableStateOf<Boleta?>(null) }
                        var error by remember { mutableStateOf<String?>(null) }

                        // Estado del formulario
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("3. Emitir Boleta", style = MaterialTheme.typography.headlineSmall)
                            Spacer(Modifier.height(16.dp))
                            OutlinedTextField(rut, { rut = it }, label = { Text("RUT Cliente") })
                            Row {
                                OutlinedTextField(anio, { anio = it }, label = { Text("Año") }, modifier = Modifier.weight(1f))
                                OutlinedTextField(mes, { mes = it }, label = { Text("Mes") }, modifier = Modifier.weight(1f))
                            }
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = {
                                try {
                                    // Se llama al servicio para poder crear o recuperar la boleta
                                    error = null
                                    boletaResult = boletaService.emitirBoletaMensual(
                                        rutCliente = rut, anio = anio.toInt(), mes = mes.toInt()
                                    )
                                    // Llama al servicio para exportar el PDF
                                    val pdfBytes = boletaService.exportarPdfClienteMes(
                                        rutCliente = rut, anio = anio.toInt(), mes = mes.toInt(), pdf = pdfService
                                    )
                                    // Se abre el PDF con la función 'abstract'
                                    onOpenPdf(pdfBytes, "boleta-$rut-$mes-$anio.pdf")
                                } catch (e: Exception) {
                                    boletaResult = null
                                    error = "Error: ${e.message}"
                                }
                            }) {
                                Text("Emitir Boleta y Abrir PDF")
                            }


                            // Se muestra el resultado de la boleta
                            Spacer(Modifier.height(16.dp))
                            boletaResult?.let {
                                Text("Boleta Generada:", style = MaterialTheme.typography.titleMedium)
                                Text("Cliente: ${it.idCliente}")
                                Text("Total a Pagar: $${it.detalle.total}")
                            }
                            // Error al crear la boleta
                            error?.let {
                                Text(it, color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }
}