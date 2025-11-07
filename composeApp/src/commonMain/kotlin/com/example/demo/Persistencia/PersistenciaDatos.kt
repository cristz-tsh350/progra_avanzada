// Fichero: composeApp/src/commonMain/kotlin/com.example.demo/PersistenciaDatos.kt
package com.example.demo.Persistencia

import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente
import com.example.demo.Dominio.EstadoCliente
import com.example.demo.Dominio.LecturaConsumo
import com.example.demo.Dominio.Medidor
import com.example.demo.Dominio.TipoTarifa
import com.example.demo.Persistencia.BoletaRepositorio
import com.example.demo.Persistencia.ClienteRepositorio
import com.example.demo.Persistencia.LecturaRepositorio
import com.example.demo.Persistencia.MedidorRepositorio

// (Asumiendo que todas tus clases como Cliente, Medidor, etc.
//  están en el paquete 'com.example.demo')

class PersistenciaDatos : ClienteRepositorio, MedidorRepositorio, LecturaRepositorio, BoletaRepositorio {

    // --- 1. AÑADE ESTE CLIENTE DE PRUEBA ---
    private val clienteDePrueba = Cliente(
        rut = "1-9", // <-- El RUT por defecto de tu UI
        nombre = "Cliente de Prueba",
        email = "prueba@cge.cl",
        direccionFacturacion = "Av. Siempre Viva 123",
        estado = EstadoCliente.ACTIVO,
        tipoTarifa = TipoTarifa.RESIDENCIAL // <-- Usa el tipo de tarifa
    )

    // --- 2. MODIFICA ESTA LÍNEA ---
    // Inicia el mapa con el cliente de prueba adentro
    private val clientes = mutableMapOf<String, Cliente>(
        clienteDePrueba.rut to clienteDePrueba
    )

    // El resto de tus listas (medidores, lecturas, boletas) pueden seguir vacías
    private val medidores = mutableListOf<Medidor>()
    private val lecturas = mutableListOf<LecturaConsumo>()
    private val boletas = mutableListOf<Boleta>()

    // ... (El resto de tus funciones override)
    // Implementaciones de ClienteRepositorio
    override fun obtenerCliente(rut: String): Cliente? = clientes[rut]
    override fun guardarCliente(cliente: Cliente) { clientes[cliente.rut] = cliente }
    override fun obtenerTodosLosClientes(): Map<String, Cliente> = clientes.toMap()

    // (Implementaciones de MedidorRepositorio)
    override fun obtenerMedidoresCliente(idCliente: String): List<Medidor> {
        return medidores
    }
    override fun guardarMedidor(medidor: Medidor) { medidores.add(medidor) }

    // (Implementaciones de LecturaRepositorio)
    override fun obtenerLectura(idMedidor: String, anio: Int, mes: Int): LecturaConsumo? {
        return lecturas.find { it.idMedidor == idMedidor && it.anio == anio && it.mes == mes }
    }
    override fun guardarLectura(lectura: LecturaConsumo) { lecturas.add(lectura) }

    // (Implementaciones de BoletaRepositorio)
    override fun obtenerBoleta(idCliente: String, anio: Int, mes: Int): Boleta? {
        return boletas.find { it.idCliente == idCliente && it.anio == anio && it.mes == mes }
    }
    override fun guardarBoleta(boleta: Boleta) { boletas.add(boleta) }
    override fun obtenerTodasLasBoletas(): List<Boleta> = boletas.toList()
}