package com.example.proyecto_01_prestamos_bancarios

import java.util.*

// Define la clase Prestamo
data class Prestamo(
    val tipo: String,
    val monto: Double,
    val plazo: String,
    val tasaInteres: Double,
    val montoCuota: Double,
    val fechaCreacion: Date,
    val fechaFinalizacion: Date
) {
    // Define el método toString
    override fun toString(): String {
        return "Prestamo(tipo='$tipo', monto=$monto, plazo='$plazo', tasaInteres=$tasaInteres, montoCuota=$montoCuota, fechaCreacion=$fechaCreacion, fechaFinalizacion=$fechaFinalizacion)"
    }
}