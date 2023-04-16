package com.example.proyecto_01_prestamos_bancarios

data class Cliente(
    var id: String? = null,
    var nombre: String? = null,
    var apellido: String? = null,
    var email: String? = null,
    var salario: String? = null,
    var telefono: String? = null,
    var direccion: String? = null
) {
    constructor() : this(null, null, null, null,null, null, null)
}





