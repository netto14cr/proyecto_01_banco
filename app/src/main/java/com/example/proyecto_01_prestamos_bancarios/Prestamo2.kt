package com.example.proyecto_01_prestamos_bancarios

import java.io.Serializable
import java.util.*

class Prestamo2 : Serializable {
    var id: String = ""
    var nombre: String = ""
    var tipo: String = ""
    var monto: Double = 0.0
    var plazo: String = ""
    var tasaInteres: Double = 0.0
    var montoCuota: Double = 0.0
    var fechaCreacion: Date? = null
    var fechaFinalizacion: Date? = null
    var saldo: Double = 0.0
    var estado: String = ""

    constructor()

    constructor(id: String, tipo: String, monto: Double, plazo: String, tasaInteres: Double, montoCuota: Double, fechaCreacion: Date?, fechaFinalizacion: Date?, saldo: Double, estado: String) {
        this.id = id
        this.tipo = tipo
        this.monto = monto
        this.plazo = plazo
        this.tasaInteres = tasaInteres
        this.montoCuota = montoCuota
        this.fechaCreacion = fechaCreacion
        this.fechaFinalizacion = fechaFinalizacion
        this.saldo = saldo
        this.estado = estado
    }

    constructor(id: String, nombre: String, tipo: String, monto: Double, plazo: String, tasaInteres: Double, montoCuota: Double, fechaCreacion: Date?, fechaFinalizacion: Date?, saldo: Double, estado: String) {
        this.id = id
        this.nombre = nombre
        this.tipo = tipo
        this.monto = monto
        this.plazo = plazo
        this.tasaInteres = tasaInteres
        this.montoCuota = montoCuota
        this.fechaCreacion = fechaCreacion
        this.fechaFinalizacion = fechaFinalizacion
        this.saldo = saldo
        this.estado = estado
    }
}
