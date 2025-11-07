package com.example.demo.Dominio

import com.example.demo.Dominio.EntidadBase
import com.example.demo.Dominio.EstadoBoleta
import com.example.demo.Dominio.ExportablePDF
import com.example.demo.Dominio.PdfTable
import com.example.demo.Dominio.TarifaDetalle
import kotlin.time.Instant

data class Boleta(
    val idCliente: String,
    val anio: Int,
    val mes: Int,
    val kwhTotal: Double,
    val detalle: TarifaDetalle,
    var estado: EstadoBoleta
) : EntidadBase(), ExportablePDF { // Hereda de EntidadBase e implementa ExportablePDF

    // Simulación de EntidadBase
    // ID único compuesto para la boleta
    override val id: String = "$idCliente-$anio-$mes"
    override val createdAt: String = ""
    override val updatedAt: String = ""

    // Implementación de ExportablePDF
    // Convierte los datos de esta boleta en un formato de tabla simple, que luego es usado por el generador de PDF
    override fun toPdfTable(): PdfTable {
        val headers = listOf("Concepto", "Valor")
        val rows = listOf(
            listOf("ID Cliente", idCliente),
            listOf("Periodo", "$mes/$anio"),
            listOf("KWh Consumidos", kwhTotal.toString()),
            // --- LÍNEAS MODIFICADAS ---
            listOf("Subtotal", "$${detalle.subtotal}"),
            listOf("Cargos", "$${detalle.cargos}"),
            listOf("IVA", "$${detalle.iva}"),
            listOf("TOTAL", "$${detalle.total}")
        )
        return PdfTable(headers, rows) //
    }
}