package com.example.demo.Persistencia
import com.example.demo.Dominio.Medidor

// Fichero: persistencia/MedidorRepositorio.kt

interface MedidorRepositorio {
    fun obtenerMedidoresCliente(idCliente: String): List<Medidor>
    fun guardarMedidor(medidor: Medidor)
}