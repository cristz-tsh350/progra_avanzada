// Fichero: dominio/MedidorMonofasico.kt
// [cite: 150]
package com.example.demo.Dominio

// Hereda de Medidor
class MedidorMonofasico(
    codigo: String,
    direccionSuministro: String,
    activo: Boolean,
    val potenciaMaxKw: Double // [cite: 151]
) : Medidor(codigo, direccionSuministro, activo) {

    // Simulación de EntidadBase
    override val id: String = codigo
    override val createdAt: String = ""
    override val updatedAt: String = ""

    // Implementación polimórfica [cite: 151]
    override fun tipo(): String = "Monofásico"
}