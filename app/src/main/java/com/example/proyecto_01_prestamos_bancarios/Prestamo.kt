package com.example.proyecto_01_prestamos_bancarios

import java.util.*

data class Prestamo(
    val tipoPrestamo: String,
    val montoPrestamo: Double,
    val montoSolicitado: Double,
    val saldo: Double,
    val plazoTexto: String,
    val tasaInteres: Double,
    val montoCuota: Double,
    val fechaInicio: Date,
    val fechaFinalizacion: Date,
    val prestamoActivo: Boolean,
    val cuotasTotales: Int,
    val cuotasCanceladas: Int
) {
    override fun toString(): String {
        return "Prestamo(tipoPrestamo='$tipoPrestamo', montoPrestamo=$montoPrestamo,montoSolicitado=$montoSolicitado,saldo=$saldo, plazoTexto='$plazoTexto', tasaInteres=$tasaInteres, montoCuota=$montoCuota, fechaInicio=$fechaInicio, fechaFinalizacion=$fechaFinalizacion, prestamoActivo=$prestamoActivo, cuotasTotales=$cuotasTotales, cuotasCanceladas=$cuotasCanceladas)"
    }
}
