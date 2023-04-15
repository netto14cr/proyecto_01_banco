package com.example.proyecto_01_prestamos_bancarios

import kotlin.math.pow

class Prestamo(
    val tipoPrestamo: String,
    val montoPrestamo: Double,
    val interes: Double,
    val plazo: Int,
    var montoCuota: Double
) {
    var pagosRealizados: Int = 0

    fun montoPendiente(): Double {
        val tasaMensual = interes / 12.0
        val nPagosRestantes = plazo - pagosRealizados
        val montoPendiente =
            montoPrestamo * (1 + tasaMensual).pow(nPagosRestantes) - montoCuota * ((1 + tasaMensual).pow(nPagosRestantes) - 1) / tasaMensual
        return montoPendiente
    }

    fun realizarPago() {
        pagosRealizados++
        montoCuota = calcularMontoCuota()
    }

    private fun calcularMontoCuota(): Double {
        val tasaMensual = interes / 12.0
        return montoPrestamo * tasaMensual / (1 - (1 + tasaMensual).pow(-plazo))
    }
}

