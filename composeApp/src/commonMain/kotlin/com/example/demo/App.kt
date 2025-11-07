// Fichero: composeApp/src/commonMain/kotlin/com/example/demo/App.kt
package com.example.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.isSystemInDarkTheme

// --- IMPORTACIONES CORREGIDAS ---
// (Importa las clases de sus paquetes CORRECTOS)
import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente
import com.example.demo.Dominio.EstadoCliente // <-- IMPORTACIÓN AÑADIDA
import com.example.demo.Dominio.LecturaConsumo
import com.example.demo.Dominio.TipoTarifa // <-- IMPORTACIÓN AÑADIDA
import com.example.demo.Servicios.BoletaService
import com.example.demo.Persistencia.PersistenciaDatos
import com.example.demo.Servicios.PdfService
import com.example.demo.Servicios.TarifaService

// --- Instanciamos los servicios ---
val persistencia = PersistenciaDatos()
val tarifaService = TarifaService()
val pdfService = PdfService()
val boletaService = BoletaService(
    persistencia, persistencia, persistencia, persistencia, tarifaService
)

// --- Definimos las pantallas ---
enum class Pantalla {
    REGISTRAR_CLIENTE,
    REGISTRAR_LECTURA,
    EMITIR_BOLETA
}

// --- ENUMS BORRADOS ---
// (BORRAMOS 'enum class EstadoCliente' y 'enum class TipoTarifa' DE AQUÍ
// porque ya existen en la carpeta Dominio)


@Composable
fun App(
    onOpenPdf: (bytes: ByteArray, filename: String) -> Unit
) {
    // --- LÓGICA DEL TEMA OSCURO ---
    var mutableStateOf = mutableStateOf(isSystemInDarkTheme())
    var isDarkMode by remember { mutableStateOf }
    val lightColors = lightColorScheme()
    val darkColors = darkColorScheme()
    val colorScheme = if (isDarkMode) darkColors else lightColors

    MaterialTheme(
        colorScheme = colorScheme
    ) {
        var pantallaActual by remember { mutableStateOf(Pantalla.REGISTRAR_CLIENTE) }

        Surface(modifier = Modifier.fillMaxSize()) {
            Column {
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

                when (pantallaActual) {

                    Pantalla.REGISTRAR_CLIENTE -> {
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
                                // Ahora 'TipoTarifa' y 'EstadoCliente' vienen del paquete 'Dominio'
                                val tipo = if (tipoTarifaStr.uppercase().startsWith("COMER")) TipoTarifa.COMERCIAL else TipoTarifa.RESIDENCIAL
                                val nuevoCliente = Cliente(
                                    rut = rut, nombre = nombre, email = email,
                                    direccionFacturacion = direccion,
                                    estado = EstadoCliente.ACTIVO,
                                    tipoTarifa = tipo
                                )
                                try {
                                    persistencia.guardarCliente(nuevoCliente)
                                    mensaje = "¡Cliente $nombre guardado como $tipo!"
                                    rut = ""; nombre = ""; email = ""; direccion = ""; tipoTarifaStr = "RESIDENCIAL"
                                } catch (e: Exception) {
                                    mensaje = "Error al guardar: ${e.message}"
                                }
                            }) { Text("Guardar Cliente") }
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { pantallaActual = Pantalla.REGISTRAR_LECTURA }) {
                                Text("Ir a Registrar Lectura ->")
                            }
                            mensaje?.let {
                                Text(it, color = if (it.startsWith("Error")) Color.Red else Color.Green)
                            }
                        }
                    }

                    Pantalla.REGISTRAR_LECTURA -> {
                        // ... (El código de esta pantalla no cambia)
                        var idMedidor by remember { mutableStateOf("1-9") }
                        var anio by remember { mutableStateOf("2025") }
                        var mes by remember { mutableStateOf("10") }
                        var kwhLeidos by remember { mutableStateOf("") }
                        var mensaje by remember { mutableStateOf<String?>(null) }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("2. Registrar Lectura Mensual", style = MaterialTheme.typography.headlineSmall)
                            Spacer(Modifier.height(16.dp))
                            OutlinedTextField(idMedidor, { idMedidor = it }, label = { Text("ID Medidor (RUT Cliente)") })
                            OutlinedTextField(anio, { anio = it }, label = { Text("Año (ej: 2025)") })
                            OutlinedTextField(mes, { mes = it }, label = { Text("Mes (ej: 10)") })
                            OutlinedTextField(kwhLeidos, { kwhLeidos = it }, label = { Text("KWh Leídos (ej: 1500)") })
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = {
                                try {
                                    val nuevaLectura = LecturaConsumo(
                                        idMedidor = idMedidor,
                                        anio = anio.toInt(),
                                        mes = mes.toInt(),
                                        kwhLeidos = kwhLeidos.toDouble()
                                    )
                                    persistencia.guardarLectura(nuevaLectura)
                                    mensaje = "¡Lectura de $kwhLeidos KWh guardada!"
                                    kwhLeidos = ""
                                } catch (e: Exception) {
                                    mensaje = "Error al guardar la lectura: ${e.message}"
                                }
                            }) { Text("Guardar Lectura") }
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { pantallaActual = Pantalla.REGISTRAR_CLIENTE }) {
                                Text("<- Volver a Clientes")
                            }
                            Button(onClick = { pantallaActual = Pantalla.EMITIR_BOLETA }) {
                                Text("Ir a Emitir Boleta ->")
                            }
                            mensaje?.let {
                                Text(it, color = if (it.startsWith("Error")) Color.Red else Color.Green)
                            }
                        }
                    }

                    Pantalla.EMITIR_BOLETA -> {
                        // ... (El código de esta pantalla no cambia)
                        var rut by remember { mutableStateOf("1-9") }
                        var anio by remember { mutableStateOf("2025") }
                        var mes by remember { mutableStateOf("10") }
                        var boletaResult by remember { mutableStateOf<Boleta?>(null) }
                        var error by remember { mutableStateOf<String?>(null) }

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
                                    error = null
                                    boletaResult = boletaService.emitirBoletaMensual(
                                        rutCliente = rut, anio = anio.toInt(), mes = mes.toInt()
                                    )
                                    val pdfBytes = boletaService.exportarPdfClienteMes(
                                        rutCliente = rut, anio = anio.toInt(), mes = mes.toInt(), pdf = pdfService
                                    )
                                    onOpenPdf(pdfBytes, "boleta-$rut-$mes-$anio.pdf")
                                } catch (e: Exception) {
                                    boletaResult = null
                                    error = "Error: ${e.message}"
                                }
                            }) {
                                Text("Emitir Boleta y Abrir PDF")
                            }
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { pantallaActual = Pantalla.REGISTRAR_LECTURA }) {
                                Text("<- Volver a Registrar Lectura")
                            }
                            Spacer(Modifier.height(16.dp))
                            boletaResult?.let {
                                Text("Boleta Generada:", style = MaterialTheme.typography.titleMedium)
                                Text("Cliente: ${it.idCliente}")
                                Text("Total a Pagar: $${it.detalle.total}")
                            }
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