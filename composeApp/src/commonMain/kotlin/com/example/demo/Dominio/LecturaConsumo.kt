package com.example.demo.Dominio
data class LecturaConsumo(
    val idMedidor: String,
    val anio: Int,
    val mes: Int,
    val kwhLeidos: Double
) : EntidadBase() {

    override val id: String = "$idMedidor-$anio-$mes"
    override val createdAt: String = ""
    override val updatedAt: String = ""
}