package com.example.proyecto_01_prestamos_bancarios

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnCancel: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Asignar los campos de entrada y botones de la interfaz a variables de Kotlin
        etUsername = findViewById(R.id.edit_text_email)
        etPassword = findViewById(R.id.edit_text_password)
        btnLogin = findViewById(R.id.button_login)
        btnCancel = findViewById(R.id.button_back)

        // Inicializar Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Asignar un listener al botón "Ingresar"
        btnLogin.setOnClickListener {
            // Obtener los valores de usuario y contraseña ingresados
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            // Validar que los campos de usuario y contraseña no estén vacíos
            if (username.isEmpty() || password.isEmpty()) {
                showMessageDialog("Error", "Por favor ingrese su correo electrónico y contraseña.")
                return@setOnClickListener
            }

            // Iniciar sesión con Firebase Authentication
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Si el inicio de sesión fue exitoso, obtener el usuario actual y mostrar un mensaje de bienvenida
                        val user = auth.currentUser
                        val message = "Bienvenido, ${user?.email}!"
                        showMessageDialog("¡Éxito!", message)

                        // Ir a la actividad principal de la app
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish() // Cerrar esta actividad para que no se pueda volver a ella con el botón "Atrás"
                    } else {
                        // Si el inicio de sesión falló, mostrar un mensaje de error
                        showMessageDialog("Error", "Credenciales inválidas. Por favor, inténtelo nuevamente.")
                    }
                }
        }

        // Asignar un listener al botón "Cancelar"
        btnCancel.setOnClickListener {
            // Ir a la actividad anterior (en este caso, la actividad Main)
            finish() // Cerrar esta actividad para que no se pueda volver a ella con el botón "Atrás"
        }
    }

    // Función para mostrar un diálogo con un mensaje personalizado
    private fun showMessageDialog(title: String, message: String) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(title)
        dialog.setMessage(message)
        dialog.setPositiveButton("OK", null)
        dialog.show()
    }
}
