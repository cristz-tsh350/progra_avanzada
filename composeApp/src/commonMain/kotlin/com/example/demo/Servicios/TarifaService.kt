// Fichero: servicios/TarifaService.kt
// [cite: 87]
package com.example.demo.Servicios

import com.example.demo.Dominio.Cliente
import com.example.demo.Dominio.Tarifa
import com.example.demo.Dominio.TarifaResidencial

// Servicio para decidir qué tarifa aplicar
class TarifaService {

    // Lógica simple: por ahora todos son residenciales
    // (En un caso real, esto se basaría en el cliente)
    fun tarifaParaCliente(cliente: Cliente): Tarifa
    { // [cite: 88]
        // Aquí se podría implementar lógica para decidir
        // si es TarifaComercial o TarifaResidencial
        return TarifaResidencial(550.0, 120.5, 0.19)
    }
}