package com.example.proyecto_01_prestamos_bancarios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnCancel: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Asignar los campos de entrada y botones de la interfaz a variables de Kotlin
        etUsername = findViewById(R.id.edit_text_email)
        etPassword = findViewById(R.id.edit_text_password)
        btnLogin = findViewById(R.id.button_login)
        btnCancel = findViewById(R.id.button_back)

        // Asignar un listener al botón "Ingresar"
        btnLogin.setOnClickListener {
            // Obtener los valores de usuario y contraseña ingresados
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            // Aquí iría la lógica de verificación de credenciales y autenticación
            // ...

            // Si las credenciales son válidas, ir a la actividad principal de la app
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Cerrar esta actividad para que no se pueda volver a ella con el botón "Atrás"
        }

        // Asignar un listener al botón "Cancelar"
        btnCancel.setOnClickListener {
            // Ir a la actividad anterior (en este caso, la actividad Main)
            finish() // Cerrar esta actividad para que no se pueda volver a ella con el botón "Atrás"
        }
    }
}
