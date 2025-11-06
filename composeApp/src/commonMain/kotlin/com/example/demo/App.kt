// Fichero: composeApp/src/commonMain/kotlin/com/example/demo/App.kt
package com.example.demo

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente
import com.example.demo.Dominio.EstadoCliente
import com.example.demo.Persistencia.PersistenciaDatos
import com.example.demo.Servicios.BoletaService
import com.example.demo.Servicios.PdfService
import com.example.demo.Servicios.TarifaService

// ... (tus 'val persistencia = ...' y 'enum Pantalla' van aquí) ...
val persistencia = PersistenciaDatos()
val tarifaService = TarifaService()
val pdfService = PdfService()
val boletaService = BoletaService(
    persistencia, persistencia, persistencia, persistencia, tarifaService
)
enum class Pantalla { REGISTRAR_CLIENTE, EMITIR_BOLETA }
enum class EstadoCliente { ACTIVO, INACTIVO }


@Composable
fun App(
    // 1. ACEPTAMOS LA FUNCIÓN como un parámetro
    onOpenPdf: (bytes: ByteArray, filename: String) -> Unit
) {
    MaterialTheme {
        var pantallaActual by remember { mutableStateOf(Pantalla.REGISTRAR_CLIENTE) }

        when (pantallaActual) {

            Pantalla.REGISTRAR_CLIENTE -> {
                // ... (Tu código de registro de cliente no cambia) ...
                var rut by remember { mutableStateOf("") }
                var nombre by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var direccion by remember { mutableStateOf("") }
                var mensaje by remember { mutableStateOf<String?>(null) }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text("1. Registrar Cliente", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(rut, { rut = it }, label = { Text("RUT Cliente") })
                    OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") })
                    OutlinedTextField(email, { email = it }, label = { Text("Email") })
                    OutlinedTextField(direccion, { direccion = it }, label = { Text("Dirección Facturación") })
                    Spacer(Modifier.height(16.dp))

                    Button(onClick = {
                        val nuevoCliente = Cliente(
                            rut = rut, nombre = nombre, email = email,
                            direccionFacturacion = direccion, estado = EstadoCliente.ACTIVO
                        )
                        try {
                            persistencia.guardarCliente(nuevoCliente)
                            mensaje = "¡Cliente $nombre guardado con éxito!"
                            rut = ""; nombre = ""; email = ""; direccion = ""
                        } catch (e: Exception) {
                            mensaje = "Error al guardar: ${e.message}"
                        }
                    }) { Text("Guardar Cliente") }

                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { pantallaActual = Pantalla.EMITIR_BOLETA }) {
                        Text("Ir a Emitir Boleta ->")
                    }
                    mensaje?.let {
                        Text(it, color = if (it.startsWith("Error")) Color.Red else Color.Green)
                    }
                }
            }

            Pantalla.EMITIR_BOLETA -> {
                var rut by remember { mutableStateOf("1-9") }
                var anio by remember { mutableStateOf("2025") }
                var mes by remember { mutableStateOf("10") }
                var boletaResult by remember { mutableStateOf<Boleta?>(null) }
                var error by remember { mutableStateOf<String?>(null) }

                Column(modifier = Modifier.padding(16.dp)) {
                    Text("2. Emitir Boleta", style = MaterialTheme.typography.headlineSmall)
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

                            // 2. ¡USAMOS LA FUNCIÓN QUE NOS PASARON!
                            onOpenPdf(pdfBytes, "boleta-$rut-$mes-$anio.pdf")

                        } catch (e: Exception) {
                            boletaResult = null
                            error = "Error: ${e.message}"
                        }
                    }) {
                        Text("Emitir Boleta y Abrir PDF") // <-- Texto actualizado
                    }

                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { pantallaActual = Pantalla.REGISTRAR_CLIENTE }) {
                        Text("<- Volver a Registrar Cliente")
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