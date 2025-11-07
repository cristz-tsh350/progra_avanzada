package com.example.demo

import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.font.Standard14Fonts
import java.io.ByteArrayOutputStream
import java.io.IOException // <-- AÑADIR ESTA LÍNEA

// Implementación REAL (actual) para JVM
actual fun generarPdfPlataforma(boletas: List<Boleta>, clientes: Map<String, Cliente>): ByteArray {
    val outputStream = ByteArrayOutputStream()
    val document = PDDocument()

    try {
        val font = PDType1Font(Standard14Fonts.FontName.HELVETICA)
        val fontBold = PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD)

        boletas.forEach { boleta ->
            val cliente = clientes[boleta.idCliente]
            val page = PDPage()
            document.addPage(page)
            val contentStream = PDPageContentStream(document, page)

            contentStream.beginText()
            contentStream.setFont(fontBold, 16f)
            contentStream.setLeading(18f)
            contentStream.newLineAtOffset(50f, 700f)

            contentStream.showText("--- BOLETA CGE ---")
            contentStream.newLine()
            contentStream.newLine()

            contentStream.setFont(font, 12f)
            contentStream.setLeading(14.5f)

            contentStream.showText("Cliente: ${cliente?.nombre ?: "Desconocido"} (${boleta.idCliente})")
            contentStream.newLine()
            contentStream.showText("Dirección: ${cliente?.direccionFacturacion ?: "N/A"}")
            contentStream.newLine()
            contentStream.showText("Periodo: ${boleta.mes}/${boleta.anio}")
            contentStream.newLine()
            contentStream.newLine()
            contentStream.showText("----------------------------------------------------------")
            contentStream.newLine()

            // Usamos la tabla de la boleta
            val tabla = boleta.toPdfTable()
            tabla.rows.forEach { row ->
                // Formateamos como "Concepto: Valor"
                val concepto = row[0].padEnd(20)
                val valor = row[1]
                contentStream.showText("$concepto: $valor")
                contentStream.newLine()
            }

            contentStream.showText("----------------------------------------------------------")
            contentStream.newLine()
            contentStream.setFont(fontBold, 14f)
            val totalRow = tabla.rows.last() // El total es la última fila
            contentStream.showText("${totalRow[0].padEnd(20)}: ${totalRow[1]}")
            contentStream.newLine()

            contentStream.endText()
            contentStream.close()
        }
    } catch (e: Exception) {
        document.close()
        // Ahora 'IOException' será reconocido
        throw IOException("Error generando el PDF", e)
    }

    document.save(outputStream)
    document.close()
    return outputStream.toByteArray()
}