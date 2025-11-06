// Fichero: dominio/Medidor.kt
// [cite: 136]
package com.example.demo.Dominio
// Clase base abstracta para Herencia [cite: 31, 54]
abstract class Medidor(
    val codigo: String, // [cite: 137]
    val direccionSuministro: String, // [cite: 138]
    var activo: Boolean // [cite: 139]
) : EntidadBase() {
    abstract fun tipo(): String
}