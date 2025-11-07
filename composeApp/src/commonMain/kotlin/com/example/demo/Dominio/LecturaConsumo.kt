// Fichero: dominio/LecturaConsumo.kt
// [cite: 146]
package com.example.demo.Dominio


data class LecturaConsumo(
    val idMedidor: String, // [cite: 147]
    val anio: Int, // [cite: 147]
    val mes: Int, // [cite: 148]
    val kwhLeidos: Double // [cite: 149]
) : EntidadBase() {

    // Simulación de EntidadBase
    override val id: String = "$idMedidor-$anio-$mes"
    override val createdAt: String = ""
    override val updatedAt: String = ""
}