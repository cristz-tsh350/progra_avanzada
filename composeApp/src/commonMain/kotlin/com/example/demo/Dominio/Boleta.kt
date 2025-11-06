

package com.example.demo.Dominio

import kotlin.time.Instant

data class Boleta(
    val idCliente: String, // [cite: 141]
    val anio: Int, // [cite: 142]
    val mes: Int, // [cite: 142]
    val kwhTotal: Double, // [cite: 143]
    val detalle: TarifaDetalle, // [cite: 144]
    var estado: EstadoBoleta // [cite: 144]
) : EntidadBase(), ExportablePDF { // [cite: 156]

    // Simulación de EntidadBase
    override val id: String = "$idCliente-$anio-$mes"
    override val createdAt: String = ""
    override val updatedAt: String = ""

    // Implementación de la interfaz [cite: 157]
    override fun toPdfTable(): PdfTable {
        val headers = listOf("Concepto", "Valor")
        val rows = listOf(
            listOf("ID Cliente", idCliente),
            listOf("Periodo", "$mes/$anio"),
            listOf("KWh Consumidos", kwhTotal.toString()),
            listOf("Subtotal", detalle.subtotal.toString()),
            listOf("Cargos", detalle.cargos.toString()),
            listOf("IVA", detalle.iva.toString()),
            listOf("TOTAL", detalle.total.toString())
        )
        return PdfTable(headers, rows) // [cite: 145]
    }
}