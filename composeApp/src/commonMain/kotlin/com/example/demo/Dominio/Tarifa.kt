package com.example.demo.Dominio
interface Tarifa {
    // Devuelve el nombre legible de la tarifa (ej: "Tarifa Residencial")
    fun nombre(): String

    //Calcula el detalle de una tarifa basado en los KWh consumidos
    fun calcular(kwh: Double): TarifaDetalle
}