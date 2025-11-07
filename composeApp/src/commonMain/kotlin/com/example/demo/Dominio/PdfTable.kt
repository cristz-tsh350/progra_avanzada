package com.example.demo.Dominio
// Clase de datos para la exportación a PDF
data class PdfTable(
    val headers: List<String>,
    val rows: List<List<String>>
)