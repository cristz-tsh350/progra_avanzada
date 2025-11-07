package com.example.demo.Dominio

class MedidorTrifasico(
    codigo: String,
    direccionSuministro: String,
    activo: Boolean,
    val potenciaMaxKw: Double,
    val factorPotencial: Double
) : Medidor(codigo, direccionSuministro, activo) {

    // Simulación de EntidadBase
    override val id: String = codigo
    override val createdAt: String = ""
    override val updatedAt: String = ""

    // Implementación del polimorfismo
    override fun tipo(): String = "Trifásico"
}