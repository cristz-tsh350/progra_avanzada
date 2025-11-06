
package com.example.demo.Dominio


// Clase base abstracta como se indica en el UML [cite: 134]
abstract class EntidadBase {
    abstract val id: String
    abstract val createdAt: String // <-- Cambiado de Instant a String
    abstract val updatedAt: String // <-- Cambiado de Instant a String
}