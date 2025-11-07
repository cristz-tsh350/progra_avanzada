package com.example.demo.Dominio

// Implementación concreta de la Tarifa para clientes Residenciales
class TarifaResidencial(
    val cargoFijo: Double,
    val precioKwh: Double,
    val iva: Double
) : Tarifa {

    override fun nombre(): String = "Tarifa Residencial"

    override fun calcular(kwh: Double): TarifaDetalle {
        val subtotal = (kwh * precioKwh)
        val cargos = cargoFijo
        val ivaMonto = (subtotal + cargos) * iva
        val total = subtotal + cargos + ivaMonto

        return TarifaDetalle(kwh, subtotal, cargos, ivaMonto, total)
    }
}