package com.example.demo.Servicios

import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.EstadoBoleta
import com.example.demo.Persistencia.BoletaRepositorio
import com.example.demo.Persistencia.ClienteRepositorio
import com.example.demo.Persistencia.LecturaRepositorio
import com.example.demo.Persistencia.MedidorRepositorio

class BoletaService(
    // 2. Define sus dependencias
    private val clientes: ClienteRepositorio,
    private val medidores: MedidorRepositorio,
    private val lecturas: LecturaRepositorio,
    private val boletas: BoletaRepositorio,
    private val tarifas: TarifaService
) {

    // 3. Añade la lógica de cálculo de KWh
    fun calcularKwhClienteMes(rutCliente: String, anio: Int, mes: Int): Double {
        val idMedidor = rutCliente // Asumimos que el medidor es el RUT

        val lecturaActual = lecturas.obtenerLectura(idMedidor, anio, mes)
            ?: throw Exception("Lectura no encontrada para $mes/$anio. (Recuerda registrarla)")

        val (anioAnterior, mesAnterior) = if (mes == 1) { (anio - 1) to 12 } else { anio to (mes - 1) }

        val lecturaAnterior = lecturas.obtenerLectura(idMedidor, anioAnterior, mesAnterior)
        val kwhAnterior = lecturaAnterior?.kwhLeidos ?: 0.0
        val consumoDelMes = lecturaActual.kwhLeidos - kwhAnterior

        if (consumoDelMes < 0) {
            throw Exception("Error: Lectura actual (${lecturaActual.kwhLeidos}) es menor que la anterior ($kwhAnterior).")
        }
        return consumoDelMes
    }

    // 4. Añade la lógica de emisión de boleta
    fun emitirBoletaMensual(rutCliente: String, anio: Int, mes: Int): Boleta {
        val cliente = clientes.obtenerCliente(rutCliente)
            ?: throw Exception("Cliente '$rutCliente' no encontrado. (Recuerda registrarlo)")

        val kwhConsumidos = calcularKwhClienteMes(rutCliente, anio, mes)
        val tarifaAplicable = tarifas.tarifaParaCliente(cliente) // Polimorfismo
        val detalle = tarifaAplicable.calcular(kwhConsumidos)

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

    // 5. Añade la lógica de exportación
    fun exportarPdfClienteMes(rutCliente: String, anio: Int, mes: Int, pdf: PdfService): ByteArray {
        val boleta = boletas.obtenerBoleta(rutCliente, anio, mes)
            ?: throw Exception("Boleta no encontrada")
        val cliente = clientes.obtenerCliente(rutCliente)
            ?: throw Exception("Cliente no encontrado")
        return pdf.generarBoletasPDF(listOf(boleta), mapOf(rutCliente to cliente))
    }
}