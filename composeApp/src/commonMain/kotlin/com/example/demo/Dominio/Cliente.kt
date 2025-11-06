

package com.example.demo.Dominio

// Cliente hereda de Persona [cite: 102]
class Cliente(
    rut: String,
    nombre: String,
    email: String,
    val direccionFacturacion: String,
    var estado: EstadoCliente
) : Persona(rut, nombre, email) {

    // Simulación de los campos
    override val id: String = rut
    // Simplemente ponemos un string vacío
    override val createdAt: String = "" // <-- Cambiado
    override val updatedAt: String = ""

    // Cliente también implementa ExportablePDF [cite: 156]
    // (Lógica de toPdfTable no implementada)
}