// Fichero: servicios/PdfService.kt
// [cite: 85]
package com.example.demo.Servicios

import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente

// Servicio para generar el PDF (simulado)
class PdfService {

    // Genera un PDF (simulado como ByteArray) [cite: 56]
    fun generarBoletasPDF(boletas: List<Boleta>, clientes: Map<String, Cliente>): ByteArray { // [cite: 86]
        val builder = StringBuilder()
        builder.append("--- REPORTE DE BOLETAS CGE ---\n\n")

        for (boleta in boletas) {
            val cliente = clientes[boleta.idCliente]
            builder.append("===================================\n")
            builder.append("Cliente: ${cliente?.nombre ?: "Desconocido"} (${boleta.idCliente})\n")
            builder.append("Periodo: ${boleta.mes}/${boleta.anio}\n")
            builder.append("-----------------------------------\n")

            val tabla = boleta.toPdfTable() // [cite: 145]
            tabla.rows.forEach { row ->
                builder.append("${row[0].padEnd(20)}: ${row[1]}\n")
            }
            builder.append("===================================\n\n")
        }

        // En KMP real, aquí se usaría una librería PDF
        return builder.toString().encodeToByteArray()
    }
}