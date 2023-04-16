package com.example.proyecto_01_prestamos_bancarios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class InformacionPersonal : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_personal)

        Toast.makeText(
            this@InformacionPersonal,
            "Información personal principal",
            Toast.LENGTH_SHORT
        ).show()

    }
}