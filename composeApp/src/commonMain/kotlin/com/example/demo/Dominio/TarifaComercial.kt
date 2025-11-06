// Fichero: dominio/TarifaComercial.kt
// [cite: 131]
package com.example.demo.Dominio
// Implementa la interfaz Tarifa [cite: 105]
class TarifaComercial(
    val cargoFijo: Double, // [cite: 132]
    val precioKwh: Double, // [cite: 133]
    val recargoComercial: Double, // [cite: 133]
    val iva: Double // [cite: 134]
) : Tarifa {

    override fun nombre(): String = "Tarifa Comercial"

    // Implementación polimórfica [cite: 37, 107]
    override fun calcular(kwh: Double): TarifaDetalle {
        val subtotal = (kwh * precioKwh)
        val cargos = cargoFijo + recargoComercial // Recargo se suma [cite: 37]
        val ivaMonto = (subtotal + cargos) * iva
        val total = subtotal + cargos + ivaMonto

        return TarifaDetalle(kwh, subtotal, cargos, ivaMonto, total)
    }
}