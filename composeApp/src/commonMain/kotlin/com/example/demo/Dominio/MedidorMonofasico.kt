package com.example.demo.Dominio

class MedidorMonofasico(
    codigo: String,
    direccionSuministro: String,
    activo: Boolean,
    val potenciaMaxKw: Double // [cite: 151]
) : Medidor(codigo, direccionSuministro, activo) {

    override val id: String = codigo
    override val createdAt: String = ""
    override val updatedAt: String = ""

    override fun tipo(): String = "Monofásico"
}