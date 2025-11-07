package com.example.demo.Servicios

import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente
import com.example.demo.generarPdfPlataforma // <-- IMPORTAR EL EXPECT

class PdfService {

    // Simulación que crea TEXTO (para cumplir el requisito sin librerías externas)
    fun generarBoletasPDF(boletas: List<Boleta>, clientes: Map<String, Cliente>): ByteArray {

        // --- LÍNEAS MODIFICADAS ---
        // Borramos la simulación de texto y llamamos a la función de plataforma
        return generarPdfPlataforma(boletas, clientes)
        // --- FIN DE LA MODIFICACIÓN ---
    }
}