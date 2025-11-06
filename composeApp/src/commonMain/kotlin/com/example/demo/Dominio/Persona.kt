// Fichero: dominio/Persona.kt
// [cite: 102]
package com.example.demo.Dominio
// Clase base para Cliente y Operador
abstract class Persona(
    val rut: String, // [cite: 112]
    val nombre: String, // [cite: 103]
    val email: String // [cite: 104]
) : EntidadBase() // Persona hereda de EntidadBase