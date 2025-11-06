// Fichero: composeApp/src/jvmMain/kotlin/com/example/demo/Main.kt
package com.example.demo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Desktop
import java.io.File

// 1. IMPORTAMOS las clases de Java para abrir archivos

// 2. CREAMOS LA FUNCIÓN para abrir el PDF en JVM
fun openPdfDesktop(bytes: ByteArray, filename: String) {
    // 1. Crear un archivo temporal
    val tempFile = File.createTempFile("boleta-", ".pdf")
    tempFile.deleteOnExit() // Borrarlo al cerrar la app

    // 2. Escribir los bytes del PDF en el archivo
    tempFile.writeBytes(bytes)

    // 3. Usar el 'Desktop' de Java para abrir el archivo
    try {
        Desktop.getDesktop().open(tempFile)
    } catch (e: Exception) {
        println("Error al abrir PDF: ${e.message}")
        // (Manejar el caso donde no hay un visor de PDF)
    }
}

// 3. PUNTO DE ENTRADA (MAIN)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CGE Desktop"
    ) {
        // 4. PASAMOS LA FUNCIÓN de abrir PDF al App() compartido
        App(
            onOpenPdf = ::openPdfDesktop
        )
    }
}