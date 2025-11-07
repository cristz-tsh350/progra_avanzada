package com.example.demo.Persistencia

import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente
import com.example.demo.Dominio.EstadoBoleta
import com.example.demo.Dominio.EstadoCliente
import com.example.demo.Dominio.LecturaConsumo
import com.example.demo.Dominio.Medidor
import com.example.demo.Dominio.TarifaDetalle
import com.example.demo.Dominio.TipoTarifa
import com.example.demo.Persistencia.BoletaRepositorio
import com.example.demo.Persistencia.ClienteRepositorio
import com.example.demo.Persistencia.LecturaRepositorio
import com.example.demo.Persistencia.MedidorRepositorio
import java.io.File
import java.io.IOException

class PersistenciaDatos : ClienteRepositorio, MedidorRepositorio, LecturaRepositorio, BoletaRepositorio {

    // Manejador de archivo para la persistencia de clientes
    private val archivoClientes = File("clientes.csv")

    // Caché en memoria de los clientes, cargada desde archivoClientes
    private val clientes: MutableMap<String, Cliente> = cargarClientes()

    // Manejador de archivo para la persistencia de boletas
    private val archivoBoletas = File("boletas.csv")
    // Caché en memoria de las boletas, cargada desde archivoBoletas
    private val boletas: MutableList<Boleta> = cargarBoletas()

    // Almacén en memoria para medidores
    private val medidores = mutableListOf<Medidor>()

    // Almacén en memoria para lecturas
    private val lecturas = mutableListOf<LecturaConsumo>()

    override fun obtenerCliente(rut: String): Cliente? = clientes[rut]
    override fun guardarCliente(cliente: Cliente) {
        clientes[cliente.rut] = cliente // Actualiza caché
        salvarClientes() // Escribe a disco
    }
    override fun obtenerTodosLosClientes(): Map<String, Cliente> = clientes.toMap()

    override fun obtenerBoleta(idCliente: String, anio: Int, mes: Int): Boleta? {
        // Busca en la lista ya cargada
        return boletas.find { it.idCliente == idCliente && it.anio == anio && it.mes == mes }
    }

    override fun guardarBoleta(boleta: Boleta) {

        // Busca si existe una boleta con el mismo ID (cliente-año-mes)
        val indiceExistente = boletas.indexOfFirst { it.id == boleta.id }

        if (indiceExistente != -1) {
            // Si existe una boleta, la reemplazamos en la lista
            println("Actualizando boleta existente en persistencia.")
            boletas[indiceExistente] = boleta
        } else {
            // Si no existe, la añadimos
            println("Añadiendo nueva boleta a persistencia.")
            boletas.add(boleta)
        }

        salvarBoletas() // Escribe la lista completa a disco
    }

    override fun obtenerTodasLasBoletas(): List<Boleta> = boletas.toList()


    // Carga los clientes desde `clientes.csv` al iniciar la clase
    // Si el archivo no existe, crea uno con un cliente de prueba
    private fun cargarClientes(): MutableMap<String, Cliente> {
        val mapaClientes = mutableMapOf<String, Cliente>()
        try {
            if (!archivoClientes.exists()) {
                val clienteDePrueba = Cliente(
                    rut = "1-9", nombre = "Cliente de Prueba", email = "prueba@cge.cl",
                    direccionFacturacion = "Av. Siempre Viva 123",
                    estado = EstadoCliente.ACTIVO, tipoTarifa = TipoTarifa.RESIDENCIAL
                )
                mapaClientes[clienteDePrueba.rut] = clienteDePrueba
                salvarClientes(mapaClientes)
                return mapaClientes
            }
            archivoClientes.forEachLine { linea ->
                if (linea.isNotBlank()) {
                    val partes = linea.split(";")
                    if (partes.size == 6) {
                        val cliente = Cliente(
                            rut = partes[0], nombre = partes[1], email = partes[2],
                            direccionFacturacion = partes[3],
                            estado = EstadoCliente.valueOf(partes[4]),
                            tipoTarifa = TipoTarifa.valueOf(partes[5])
                        )
                        mapaClientes[cliente.rut] = cliente
                    }
                }
            }
        } catch (e: Exception) {
            println("Error al CARGAR clientes: ${e.message}")
            return mutableMapOf()
        }
        return mapaClientes
    }

    // Escribe el mapa (caché) de clientes actual al archivo "clientes.csv"
    // Sobrescribe el archivo completo
    private fun salvarClientes(mapaClientes: Map<String, Cliente>) {
        try {
            val stringBuilder = StringBuilder()
            mapaClientes.values.forEach { cliente ->
                stringBuilder.append(cliente.rut).append(";")
                stringBuilder.append(cliente.nombre).append(";")
                stringBuilder.append(cliente.email).append(";")
                stringBuilder.append(cliente.direccionFacturacion).append(";")
                stringBuilder.append(cliente.estado.name).append(";")
                stringBuilder.append(cliente.tipoTarifa.name).append("\n")
            }
            archivoClientes.writeText(stringBuilder.toString())
        } catch (e: Exception) {
            println("Error al SALVAR clientes (plataforma no compatible?): ${e.message}")
        }
    }
    private fun salvarClientes() = salvarClientes(this.clientes)


    // Carga las boletas desde `boletas.csv` al iniciar la clase
    // Reconstruye los objetos `Boleta` y `TarifaDetalle` desde el CSV
    private fun cargarBoletas(): MutableList<Boleta> {
        val listaBoletas = mutableListOf<Boleta>()
        try {
            if (!archivoBoletas.exists()) {
                archivoBoletas.createNewFile() // Crea el archivo si no existe
                return listaBoletas // Devuelve lista vacía
            }

            // Si el archivo existe, leemos cada línea.
            archivoBoletas.forEachLine { linea ->
                if (linea.isNotBlank()) {
                    // Formato: idCliente;anio;mes;kwhTotal;estado;detalle.subtotal;detalle.cargos;detalle.iva;detalle.total
                    val partes = linea.split(";")
                    if (partes.size == 9) {
                        // Reconstruimos el TarifaDetalle
                        val detalle = TarifaDetalle(
                            kwh = partes[3].toDouble(), // kwhTotal
                            subtotal = partes[5].toDouble(),
                            cargos = partes[6].toDouble(),
                            iva = partes[7].toDouble(),
                            total = partes[8].toDouble()
                        )
                        // Reconstruimos la Boleta
                        val boleta = Boleta(
                            idCliente = partes[0],
                            anio = partes[1].toInt(),
                            mes = partes[2].toInt(),
                            kwhTotal = partes[3].toDouble(),
                            detalle = detalle,
                            estado = EstadoBoleta.valueOf(partes[4])
                        )
                        listaBoletas.add(boleta)
                    }
                }
            }
        } catch (e: Exception) { // Captura IO, NumberFormat, etc.
            println("Error al CARGAR boletas: ${e.message}")
            return mutableListOf()
        }
        return listaBoletas
    }

    // Escribe la lista (caché) de boletas actual al archivo `boletas.csv`
    private fun salvarBoletas() {
        try {
            val stringBuilder = StringBuilder()
            // Usamos la lista 'boletas' de la clase
            boletas.forEach { boleta ->
                // Aplanamos la boleta y su detalle en una sola línea CSV
                stringBuilder.append(boleta.idCliente).append(";")
                stringBuilder.append(boleta.anio).append(";")
                stringBuilder.append(boleta.mes).append(";")
                stringBuilder.append(boleta.kwhTotal).append(";")
                stringBuilder.append(boleta.estado.name).append(";")
                stringBuilder.append(boleta.detalle.subtotal).append(";")
                stringBuilder.append(boleta.detalle.cargos).append(";")
                stringBuilder.append(boleta.detalle.iva).append(";")
                stringBuilder.append(boleta.detalle.total).append("\n")
            }
            // Escribimos (sobrescribimos) el archivo
            archivoBoletas.writeText(stringBuilder.toString())
        } catch (e: Exception) {
            println("Error al SALVAR boletas (plataforma no compatible?): ${e.message}")
        }
    }

    override fun obtenerMedidoresCliente(idCliente: String): List<Medidor> {
        return medidores
    }
    override fun guardarMedidor(medidor: Medidor) { medidores.add(medidor) }

    override fun obtenerLectura(idMedidor: String, anio: Int, mes: Int): LecturaConsumo? {
        return lecturas.find { it.idMedidor == idMedidor && it.anio == anio && it.mes == mes }
    }
    override fun guardarLectura(lectura: LecturaConsumo) { lecturas.add(lectura) }
}