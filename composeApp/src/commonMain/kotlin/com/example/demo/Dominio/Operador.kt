// Fichero: dominio/Operador.kt
// [cite: 127]
package com.example.demo.Dominio

// Operador hereda de Persona [cite: 102]
class Operador(
    rut: String,
    nombre: String,
    email: String,
    val perfil: String // [cite: 128]
) : Persona(rut, nombre, email) {

    // Simulación de los campos de EntidadBase
    override val id: String = rut
    override val createdAt: String = ""
    override val updatedAt: String = ""
}