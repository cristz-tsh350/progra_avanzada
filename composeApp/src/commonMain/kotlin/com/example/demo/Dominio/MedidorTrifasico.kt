// Fichero: dominio/MedidorTrifasico.kt
// [cite: 152]
package com.example.demo.Dominio

// Hereda de Medidor
class MedidorTrifasico(
    codigo: String,
    direccionSuministro: String,
    activo: Boolean,
    val potenciaMaxKw: Double,
    val factorPotencial: Double // [cite: 152]
) : Medidor(codigo, direccionSuministro, activo) {

    // Simulación de EntidadBase
    override val id: String = codigo
    override val createdAt: String = ""
    override val updatedAt: String = ""

    // Implementación polimórfica [cite: 152]
    override fun tipo(): String = "Trifásico"
}