// Fichero: dominio/Tarifa.kt
// [cite: 105]
package com.example.demo.Dominio
// Interfaz para el Polimorfismo en el cálculo de tarifas [cite: 37]
interface Tarifa {
    fun nombre(): String // [cite: 106]
    fun calcular(kwh: Double): TarifaDetalle // [cite: 107]
}