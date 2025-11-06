// Fichero: dominio/TarifaResidencial.kt
// [cite: 129]
package com.example.demo.Dominio
// Implementa la interfaz Tarifa [cite: 105]
class TarifaResidencial(
    val cargoFijo: Double, // [cite: 130]
    val precioKwh: Double, // [cite: 130]
    val iva: Double // [cite: 130]
) : Tarifa {

    override fun nombre(): String = "Tarifa Residencial"

    // Implementación polimórfica [cite: 37, 107]
    override fun calcular(kwh: Double): TarifaDetalle {
        val subtotal = (kwh * precioKwh)
        val cargos = cargoFijo
        val ivaMonto = (subtotal + cargos) * iva
        val total = subtotal + cargos + ivaMonto

        return TarifaDetalle(kwh, subtotal, cargos, ivaMonto, total)
    }
}