package com.example.demo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import java.awt.Desktop
import java.io.File

fun openPdfDesktop(bytes: ByteArray, filename: String) {
    // Corrección: Guardamos como .txt ya que el contenido es texto
    val tempFile = File.createTempFile("boleta-", ".txt")
    tempFile.deleteOnExit()
    tempFile.writeBytes(bytes)
    try {
        Desktop.getDesktop().open(tempFile) // Abrirá en tu editor de texto
    } catch (e: Exception) {
        println("Error al abrir archivo: ${e.message}")
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CGE Desktop"
    ) {
        App(
            onOpenPdf = ::openPdfDesktop
        )
    }
}