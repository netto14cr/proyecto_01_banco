package com.example.proyecto_01_prestamos_bancarios

import java.io.Serializable
import java.util.*

class CuotaPagada(
    val fecha: Date,
    val cantidad: Int,
    val montoPagado: Double,
    val montoPendiente: Double,
    val uidPago: String,
    val referenciaRecibo: String
)

