package com.example.demo.Persistencia
import com.example.demo.Dominio.LecturaConsumo

// Fichero: persistencia/LecturaRepositorio.kt

interface LecturaRepositorio {
    fun obtenerLectura(idMedidor: String, anio: Int, mes: Int): LecturaConsumo?
    fun guardarLectura(lectura: LecturaConsumo)
}