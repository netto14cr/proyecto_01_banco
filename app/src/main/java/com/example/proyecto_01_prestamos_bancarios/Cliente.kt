package com.example.proyecto_01_prestamos_bancarios

class Cliente {
    var cedula: String = ""
    var nombre: String = ""
    var salario: String = ""
    var prestamos: MutableList<Prestamo> = mutableListOf()

    fun agregarPrestamo(prestamo: Prestamo) {
        prestamos.add(prestamo)
    }

    fun eliminarPrestamo(prestamo: Prestamo) {
        prestamos.remove(prestamo)
    }

    fun calcularDeudaTotal(): Double {
        var deudaTotal = 0.0
        for (prestamo in prestamos) {
            deudaTotal += prestamo.montoPendiente()
        }
        return deudaTotal
    }
}



