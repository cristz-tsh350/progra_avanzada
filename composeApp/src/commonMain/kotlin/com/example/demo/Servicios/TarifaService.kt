package com.example.demo.Servicios

import com.example.demo.Dominio.* // Importa todo de Dominio

class TarifaService {

    // Ahora usa 'when' para decidir qué tarifa devolver (Polimorfismo)
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
                recargoComercial = 250.0, // Recargo adicional
                iva = 0.19 // Diferente cálculo de IVA (aquí iría)
            )
        }
    }
}