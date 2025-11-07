package com.example.demo.Dominio
abstract class Medidor(
    val codigo: String,
    val direccionSuministro: String,
    var activo: Boolean
) : EntidadBase() {
    abstract fun tipo(): String
}