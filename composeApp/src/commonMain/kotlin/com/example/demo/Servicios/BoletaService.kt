package com.example.demo.Servicios

// Importar clases de Dominio y Persistencia
import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente
import com.example.demo.Dominio.EstadoBoleta
import com.example.demo.Persistencia.BoletaRepositorio
import com.example.demo.Persistencia.ClienteRepositorio
import com.example.demo.Persistencia.LecturaRepositorio
import com.example.demo.Persistencia.MedidorRepositorio
class BoletaService(
    // Inyección de dependencias (repositorios)
    private val clientes: ClienteRepositorio, // [cite: 90]
    private val medidores: MedidorRepositorio, // [cite: 91]
    private val lecturas: LecturaRepositorio, // [cite: 92]
    private val boletas: BoletaRepositorio, // [cite: 93]
    private val tarifas: TarifaService // [cite: 94]
) {

    // [cite: 96]
    fun calcularKwhClienteMes(rutCliente: String, anio: Int, mes: Int): Double {
        // 1. Obtener medidores del cliente
        val medidoresCliente = medidores.obtenerMedidoresCliente(rutCliente)
        if (medidoresCliente.isEmpty()) return 0.0

        // 2. Obtener lecturas (asumiendo 1 medidor para simplificar)
        val idMedidor = medidoresCliente.first().id

        // 3. Calcular consumo (Lectura actual - lectura anterior)
        val mesAnterior = if (mes == 1) 12 else mes - 1
        val anioAnterior = if (mes == 1) anio - 1 else anio

        val lecturaActual = lecturas.obtenerLectura(idMedidor, anio, mes)?.kwhLeidos ?: 0.0
        val lecturaAnterior = lecturas.obtenerLectura(idMedidor, anioAnterior, mesAnterior)?.kwhLeidos ?: 0.0

        return lecturaActual - lecturaAnterior
    }

    // [cite: 95]
    fun emitirBoletaMensual(rutCliente: String, anio: Int, mes: Int): Boleta {
        val cliente = clientes.obtenerCliente(rutCliente)
            ?: throw Exception("Cliente no encontrado")

        // 1. Calcular KWh
        val kwhConsumidos = calcularKwhClienteMes(rutCliente, anio, mes)

        // 2. Obtener tarifa (Polimorfismo)
        val tarifaAplicable = tarifas.tarifaParaCliente(cliente) // [cite: 88]

        // 3. Calcular montos
        val detalle = tarifaAplicable.calcular(kwhConsumidos) // [cite: 107]

        // 4. Crear boleta
        val boleta = Boleta(
            idCliente = rutCliente,
            anio = anio,
            mes = mes,
            kwhTotal = kwhConsumidos,
            detalle = detalle,
            estado = EstadoBoleta.EMITIDA
        )

        boletas.guardarBoleta(boleta)
        return boleta
    }

    // [cite: 97]
    fun exportarPdfClienteMes(rutCliente: String, anio: Int, mes: Int, pdf: PdfService): ByteArray {
        val boleta = boletas.obtenerBoleta(rutCliente, anio, mes)
            ?: throw Exception("Boleta no encontrada")

        val cliente = clientes.obtenerCliente(rutCliente)
            ?: throw Exception("Cliente no encontrado")

        // Usa el PdfService para generar el archivo
        return pdf.generarBoletasPDF(listOf(boleta), mapOf(rutCliente to cliente))
    }
}