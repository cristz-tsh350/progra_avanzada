package com.example.demo

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
// 1. IMPORTAMOS las clases de JS/Navegador
import kotlinx.browser.document
import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Int8Array
import org.w3c.dom.HTMLAnchorElement
import org.w3c.dom.url.URL

// 2. CREAMOS LA FUNCIÓN para descargar el PDF en el Navegador
fun openPdfWeb(bytes: ByteArray, filename: String) {
    // 1. Convertir el ByteArray de Kotlin a un ArrayBuffer de JS
    val int8Array = Int8Array(bytes.toTypedArray())
    val arrayBuffer: ArrayBuffer = int8Array.buffer

    // 2. Crear un Blob (un objeto tipo archivo) con los datos
    val blob = org.w3c.files.Blob(arrayOf(arrayBuffer), org.w3c.files.BlobPropertyBag(type = "application/pdf"))

    // 3. Crear una URL temporal en el navegador para ese Blob
    val url = URL.createObjectURL(blob)

    // 4. Crear un enlace (<a>) invisible y hacer clic en él
    val link = document.createElement("a") as HTMLAnchorElement
    link.href = url
    link.download = filename
    document.body?.appendChild(link)
    link.click()
    document.body?.removeChild(link)
    URL.revokeObjectURL(url) // Limpiar la URL temporal
}

// 3. PUNTO DE ENTRADA (MAIN)
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(viewportContainerId = "root") {
        // 4. PASAMOS LA FUNCIÓN de abrir PDF al App() compartido
        App(
            onOpenPdf = ::openPdfWeb
        )
    }
}