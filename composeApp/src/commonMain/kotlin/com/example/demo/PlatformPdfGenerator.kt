package com.example.demo

import com.example.demo.Dominio.Boleta
import com.example.demo.Dominio.Cliente

// Definimos la "espera" de una función que genera el PDF
expect fun generarPdfPlataforma(boletas: List<Boleta>, clientes: Map<String, Cliente>): ByteArray