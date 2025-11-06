package com.example.demo.Persistencia
import com.example.demo.Dominio.Cliente

// Fichero: persistencia/ClienteRepositorio.kt

// Interfaces para la capa de persistencia
// (La implementación real usaría archivos o BD [cite: 54])
interface ClienteRepositorio {
    fun obtenerCliente(rut: String): Cliente?
    fun guardarCliente(cliente: Cliente)
    fun obtenerTodosLosClientes(): Map<String, Cliente>
}