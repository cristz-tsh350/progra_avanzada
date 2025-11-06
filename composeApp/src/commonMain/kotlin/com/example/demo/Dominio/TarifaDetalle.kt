// Fichero: dominio/TarifaDetalle.kt
// [cite: 153]
package com.example.demo.Dominio
// Almacena el resultado de un cálculo de tarifa
data class TarifaDetalle(
    val kwh: Double, // [cite: 154]
    val subtotal: Double, // [cite: 154]
    val cargos: Double, // [cite: 154]
    val iva: Double, // [cite: 154]
    val total: Double // [cite: 155]
)