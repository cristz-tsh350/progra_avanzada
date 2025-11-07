package com.example.demo.Servicios

import com.example.demo.Dominio.*

// Servicio tipo Fábrica
// Debe de crear y devolver la implementación correcta de la Tarifa según el tipo de cliente

class TarifaService {

    // Devuelve la si la Tarifa es correcta (Residencial o Comercial)
    fun tarifaParaCliente(cliente: Cliente): Tarifa {
        return when (cliente.tipoTarifa) {
            TipoTarifa.RESIDENCIAL -> TarifaResidencial(
                cargoFijo = 550.0,
                precioKwh = 120.5,
                iva = 0.19
            )
            TipoTarifa.COMERCIAL -> TarifaComercial(
                cargoFijo = 800.0,
                precioKwh = 150.0,
                recargoComercial = 250.0, // Recargo adicional por ser comercial
                iva = 0.19
            )
        }
    }
}