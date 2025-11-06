// Fichero: persistencia/BoletaRepositorio.kt
package com.example.demo.Persistencia
import com.example.demo.Dominio.Boleta // <-- AÑADIR ESTA LÍNEA

interface BoletaRepositorio {
    fun obtenerBoleta(idCliente: String, anio: Int, mes: Int): Boleta?
    fun guardarBoleta(boleta: Boleta)
    fun obtenerTodasLasBoletas(): List<Boleta>
}