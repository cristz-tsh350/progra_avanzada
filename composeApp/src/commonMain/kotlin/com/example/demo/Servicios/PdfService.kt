package com.example.demo.Servicios

import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente

class PdfService {

    // Simulación que crea TEXTO (para cumplir el requisito sin librerías externas)
    fun generarBoletasPDF(boletas: List<Boleta>, clientes: Map<String, Cliente>): ByteArray {
        val builder = StringBuilder()
        builder.append("--- REPORTE DE BOLETAS CGE (Simulación) ---\n\n")

        for (boleta in boletas) {
            val cliente = clientes[boleta.idCliente]
            builder.append("===================================\n")
            builder.append("Cliente: ${cliente?.nombre ?: "Desconocido"} (${boleta.idCliente})\n")
            builder.append("Periodo: ${boleta.mes}/${boleta.anio}\n")
            builder.append("KWh Consumidos: ${boleta.kwhTotal}\n")
            builder.append("-----------------------------------\n")

            // Usamos la info de la tabla que pide el PDF
            val tabla = boleta.toPdfTable()
            tabla.rows.forEach { row ->
                builder.append("${row[0].padEnd(20)}: ${row[1]}\n")
            }
            builder.append("===================================\n\n")
        }

        return builder.toString().encodeToByteArray()
    }
}