package com.example.demo

import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente

// Generar PDF en JS
actual fun generarPdfPlataforma(boletas: List<Boleta>, clientes: Map<String, Cliente>): ByteArray {
    val builder = StringBuilder()
    builder.append("--- REPORTE DE BOLETAS (Simulación JS) ---\n\n")

    for (boleta in boletas) {
        // Construye un String con los datos de la boleta
        val cliente = clientes[boleta.idCliente]
        builder.append("===================================\n")
        builder.append("Cliente: ${cliente?.nombre ?: "Desconocido"} (${boleta.idCliente})\n")
        builder.append("Periodo: ${boleta.mes}/${boleta.anio}\n")

        // Usa la tabla
        val tabla = boleta.toPdfTable()
        tabla.rows.forEach { row ->
            builder.append("${row[0].padEnd(20)}: ${row[1]}\n")
        }
        builder.append("===================================\n\n")
    }

    // Devuelve los bytes del String
    return builder.toString().encodeToByteArray()
}