// Fichero: dominio/PdfTable.kt
// [cite: 117]
package com.example.demo.Dominio
// Clase de datos para la exportación a PDF
data class PdfTable(
    val headers: List<String>, // [cite: 118]
    val rows: List<List<String>> // [cite: 119]
)