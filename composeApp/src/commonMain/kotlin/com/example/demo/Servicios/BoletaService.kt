package com.example.demo.Servicios

import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.EstadoBoleta
import com.example.demo.Persistencia.BoletaRepositorio
import com.example.demo.Persistencia.ClienteRepositorio
import com.example.demo.Persistencia.LecturaRepositorio
import com.example.demo.Persistencia.MedidorRepositorio

class BoletaService(
    //Define las dependencias a necesitar
    private val clientes: ClienteRepositorio,
    private val medidores: MedidorRepositorio,
    private val lecturas: LecturaRepositorio,
    private val boletas: BoletaRepositorio,
    private val tarifas: TarifaService
) {

    // Se calculan los KWH consumidos en un mes específico
    fun calcularKwhClienteMes(rutCliente: String, anio: Int, mes: Int): Double {
        val idMedidor = rutCliente // El medidor es el RUT del cliente

        // Se obtiene la lectura del mes solicitado
        val lecturaActual = lecturas.obtenerLectura(idMedidor, anio, mes)
            ?: throw Exception("Lectura no encontrada para $mes/$anio. (Recuerda registrarla)")

        // Se calcula el monto del mes anterior
        val (anioAnterior, mesAnterior) = if (mes == 1) { (anio - 1) to 12 } else { anio to (mes - 1) }

        // Se obtiene la lectura anterior, esta puede ser null si es la primera lectura
        val lecturaAnterior = lecturas.obtenerLectura(idMedidor, anioAnterior, mesAnterior)
        val kwhAnterior = lecturaAnterior?.kwhLeidos ?: 0.0
        val consumoDelMes = lecturaActual.kwhLeidos - kwhAnterior

        if (consumoDelMes < 0) {
            throw Exception("Error: Lectura actual (${lecturaActual.kwhLeidos}) es menor que la anterior ($kwhAnterior).")
        }
        return consumoDelMes
    }


    // Crea o recupera la boleta mensual para un cliente
    // La boleta puede ya existir, la recupera y si no, esta se crea desde 0
    fun emitirBoletaMensual(rutCliente: String, anio: Int, mes: Int): Boleta {

        // Se verifica si la boleta ya existe en el repositorio
        val boletaExistente = boletas.obtenerBoleta(rutCliente, anio, mes)
        if (boletaExistente != null) {
            // Al existir, esta se devuelve directamente, esto para poder reemitir el PDF
            println("Boleta ya existía. Re-emitiendo la guardada.")
            return boletaExistente
        }

        // Si no existe la boleta, se continúa con la lógica original para crearla
        println("Creando nueva boleta para $rutCliente - $mes/$anio")
        val cliente = clientes.obtenerCliente(rutCliente)
            ?: throw Exception("Cliente '$rutCliente' no encontrado. (Recuerda registrarlo)")

        // Se calcula el consumo de los KWH
        val kwhConsumidos = calcularKwhClienteMes(rutCliente, anio, mes)

        // Se obtiene la información si en una tarifa RESIDENCIAL o COMERCIAL
        val tarifaAplicable = tarifas.tarifaParaCliente(cliente)

        // Calcula las tarifas usando las estrategias correspondientes
        val detalle = tarifaAplicable.calcular(kwhConsumidos)

        // Se crea la nueva boleta
        val boleta = Boleta(
            idCliente = rutCliente,
            anio = anio,
            mes = mes,
            kwhTotal = kwhConsumidos,
            detalle = detalle,
            estado = EstadoBoleta.EMITIDA
        )
        // Se guarda la nueva boleta creada
        boletas.guardarBoleta(boleta)
        return boleta
    }

    // Se exporta la boleta del cliente seleccionado a un PDF
    fun exportarPdfClienteMes(rutCliente: String, anio: Int, mes: Int, pdf: PdfService): ByteArray {
        val boleta = boletas.obtenerBoleta(rutCliente, anio, mes)
            ?: throw Exception("Boleta no encontrada")
        val cliente = clientes.obtenerCliente(rutCliente)
            ?: throw Exception("Cliente no encontrado")

        // Creación del PDF a través del PdfService
        return pdf.generarBoletasPDF(listOf(boleta), mapOf(rutCliente to cliente))
    }
}