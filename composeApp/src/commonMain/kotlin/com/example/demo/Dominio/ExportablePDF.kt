// Fichero: dominio/ExportablePDF.kt
// [cite: 156]
package com.example.demo.Dominio
// Interfaz para objetos que pueden convertirse en tabla
interface ExportablePDF {
    fun toPdfTable(): PdfTable // [cite: 157]
}