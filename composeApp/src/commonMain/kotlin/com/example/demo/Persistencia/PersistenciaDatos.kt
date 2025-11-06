// Fichero: persistencia/PersistenciaDatos.kt
package com.example.demo.Persistencia

// Importar todas las clases del Dominio que usa
import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente
import com.example.demo.Dominio.EstadoCliente
import com.example.demo.Dominio.LecturaConsumo
import com.example.demo.Dominio.Medidor
// --- FIN DE IMPORTACIONES ---

// (Esta clase implementa las 4 interfaces de este mismo paquete)
class PersistenciaDatos : ClienteRepositorio, MedidorRepositorio, LecturaRepositorio, BoletaRepositorio {
    private val clienteDePrueba = Cliente(
        rut = "1-9",
        nombre = "Juan Cliente de Prueba",
        email = "juan.perez@cge.cl",
        direccionFacturacion = "Av. Siempre Viva 123",
        estado = EstadoCliente.ACTIVO
    )
    private val clientes = mutableMapOf<String, Cliente>(clienteDePrueba.rut to clienteDePrueba)
    private val medidores = mutableListOf<Medidor>()
    private val lecturas = mutableListOf<LecturaConsumo>()
    private val boletas = mutableListOf<Boleta>()

    // Implementaciones de ClienteRepositorio
    override fun obtenerCliente(rut: String): Cliente? = clientes[rut]
    override fun guardarCliente(cliente: Cliente) { clientes[cliente.rut] = cliente }
    override fun obtenerTodosLosClientes(): Map<String, Cliente> = clientes.toMap()

    // Implementaciones de MedidorRepositorio
    override fun obtenerMedidoresCliente(idCliente: String): List<Medidor> {
        return medidores
    }
    override fun guardarMedidor(medidor: Medidor) { medidores.add(medidor) }

    // Implementaciones de LecturaRepositorio
    override fun obtenerLectura(idMedidor: String, anio: Int, mes: Int): LecturaConsumo? {
        return lecturas.find { it.idMedidor == idMedidor && it.anio == anio && it.mes == mes }
    }
    override fun guardarLectura(lectura: LecturaConsumo) { lecturas.add(lectura) }

    // Implementaciones de BoletaRepositorio
    override fun obtenerBoleta(idCliente: String, anio: Int, mes: Int): Boleta? {
        return boletas.find { it.idCliente == idCliente && it.anio == anio && it.mes == mes }
    }
    override fun guardarBoleta(boleta: Boleta) { boletas.add(boleta) }
    override fun obtenerTodasLasBoletas(): List<Boleta> = boletas.toList()
}