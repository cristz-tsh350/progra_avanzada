package com.example.demo.Dominio
class TarifaComercial(
    val cargoFijo: Double,
    val precioKwh: Double,
    val recargoComercial: Double,
    val iva: Double
) : Tarifa {

    override fun nombre(): String = "Tarifa Comercial"

    override fun calcular(kwh: Double): TarifaDetalle {
        val subtotal = (kwh * precioKwh)
        val cargos = cargoFijo + recargoComercial
        val ivaMonto = (subtotal + cargos) * iva
        val total = subtotal + cargos + ivaMonto

        return TarifaDetalle(kwh, subtotal, cargos, ivaMonto, total)
    }
}