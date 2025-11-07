package com.example.demo.Dominio
abstract class Persona(
    val rut: String,
    val nombre: String,
    val email: String
) : EntidadBase() // Persona hereda de EntidadBase