package com.example.demo.Dominio
// Interfaz para objetos que pueden convertirse en tabla
interface ExportablePDF {
    fun toPdfTable(): PdfTable
}