package com.example.proyecto_01_prestamos_bancarios

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Asignar los botones de la interfaz a variables de Kotlin
        val buttonLogin = findViewById<Button>(R.id.loginButton)
        val buttonRegister = findViewById<Button>(R.id.registerButton)

        // Asignar un listener al botón "Iniciar sesión"
        buttonLogin.setOnClickListener {
            // Abrir la actividad correspondiente a la pantalla de inicio de sesión
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Asignar un listener al botón "Registrarse"
        buttonRegister.setOnClickListener {
            // Abrir la actividad correspondiente a la pantalla de registro
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
