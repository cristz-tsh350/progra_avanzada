// Fichero: composeApp/src/webMain/kotlin/com.example.demo/main.kt
package com.example.demo

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport

// 1. Función simulada para Wasm (no puede abrir PDFs fácilmente)
fun openPdfWasm(bytes: ByteArray, filename: String) {
    println("Simulando apertura de PDF (Wasm): $filename")
    // (La apertura real de archivos en Wasm es experimental y compleja)
}

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(viewportContainerId = "root") {
        // 2. Llama a App() y le pasa la función simulada
        App(
            onOpenPdf = ::openPdfWasm
        )
    }
}