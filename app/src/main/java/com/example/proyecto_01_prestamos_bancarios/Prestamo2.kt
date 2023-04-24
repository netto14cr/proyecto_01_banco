package com.example.proyecto_01_prestamos_bancarios

import java.io.Serializable
import java.util.*

class Prestamo2 : Serializable {
    var id: String = ""
    var nombre: String = ""
    var tipoPrestamo: String = ""
    var montoPrestamo: Double = 0.0
    var montoSolicitado: Double = 0.0
    var plazoTexto: String = ""
    var tasaInteres: Double = 0.0
    var montoCuota: Double = 0.0
    var fechaInicio: Date? = null
    var fechaFinalizacion: Date? = null
    var saldo: Double = 0.0
    var prestamoActivo: Boolean = true
    var cuotasTotales: Int = 0
    var cuotasCanceladas: Int = 0


    constructor()

    constructor(
        id: String,
        tipoPrestamo: String,
        montoPrestamo: Double,
        montoSolicitado: Double,
        plazoTexto: String,
        tasaInteres: Double,
        montoCuota: Double,
        fechaInicio: Date?,
        fechaFinalizacion: Date?,
        saldo: Double,
        prestamoActivo: Boolean,
        cuotasTotales: Int,
        cuotasCanceladas: Int
    ) {
        this.id = id
        this.tipoPrestamo = tipoPrestamo
        this.montoPrestamo = montoPrestamo
        this.montoSolicitado = montoSolicitado
        this.plazoTexto = plazoTexto
        this.tasaInteres = tasaInteres
        this.montoCuota = montoCuota
        this.fechaInicio = fechaInicio
        this.fechaFinalizacion = fechaFinalizacion
        this.saldo = saldo
        this.prestamoActivo = prestamoActivo
        this.cuotasTotales = cuotasTotales
        this.cuotasCanceladas = cuotasCanceladas
    }

    constructor(
        id: String,
        nombre: String,
        tipoPrestamo: String,
        montoPrestamo: Double,
        montoSolicitado: Double,
        plazoTexto: String,
        tasaInteres: Double,
        montoCuota: Double,
        fechaInicio: Date?,
        fechaFinalizacion: Date?,
        saldo: Double,
        prestamoActivo: Boolean,
        cuotasTotales: Int,
        cuotasCanceladas: Int
    ) {
        this.id = id
        this.nombre = nombre
        this.tipoPrestamo = tipoPrestamo
        this.montoPrestamo = montoPrestamo
        this.montoSolicitado = montoSolicitado
        this.plazoTexto = plazoTexto
        this.tasaInteres = tasaInteres
        this.montoCuota = montoCuota
        this.fechaInicio = fechaInicio
        this.fechaFinalizacion = fechaFinalizacion
        this.saldo = saldo
        this.prestamoActivo = prestamoActivo
        this.cuotasTotales = cuotasTotales
        this.cuotasCanceladas = cuotasCanceladas
    }
}
