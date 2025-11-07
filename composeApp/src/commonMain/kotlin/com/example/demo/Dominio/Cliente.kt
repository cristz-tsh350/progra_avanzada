package com.example.demo.Dominio


enum class TipoTarifa {
    RESIDENCIAL,
    COMERCIAL
}

class Cliente(
    rut: String,
    nombre: String,
    email: String,
    val direccionFacturacion: String,
    var estado: EstadoCliente,
    val tipoTarifa: TipoTarifa // <-- Propiedad añadida
) : Persona(rut, nombre, email) {

    override val id: String = rut
    override val createdAt: String = ""
    override val updatedAt: String = ""
}