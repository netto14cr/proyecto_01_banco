package com.example.proyecto_01_prestamos_bancarios

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.preference.PreferenceManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

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
            val emailPattern = Patterns.EMAIL_ADDRESS
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.length > 50 || password.length > 50) {
                showMessageDialog("Error", "La longitud máxima para el usuario y la contraseña es de 50 caracteres.")
                return@setOnClickListener
            }

            // Validar que el campo de usuario no esté vacío
            if (username.isEmpty()) {
                showMessageDialog("Error", "Por favor ingrese su correo electrónico.")
                return@setOnClickListener
            }

            // Validar que el campo de contraseña no esté vacío
            if (password.isEmpty()) {
                showMessageDialog("Error", "Por favor ingrese su contraseña.")
                return@setOnClickListener
            }

            // Validar que el correo electrónico tenga un formato válido
            if (!emailPattern.matcher(username).matches()) {
                showMessageDialog("Error", "Por favor ingrese una dirección de correo electrónico válida.")
                return@setOnClickListener
            }

            // Iniciar sesión con Firebase Authentication
            auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Si el inicio de sesión fue exitoso, obtener el usuario actual
                        val user = auth.currentUser

                        // Obtener información adicional del usuario de la base de datos
                        val userRef = FirebaseDatabase.getInstance().getReference("usuarios/${user?.uid}")
                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val tipoUsuario = dataSnapshot.child("tipo_usuario").getValue(String::class.java)?.toLowerCase(
                                    Locale.ROOT)
                                // Redirigir al usuario a la actividad correspondiente basándose en su tipo de usuario
                                if (tipoUsuario == "cliente") {
                                    val userRef = FirebaseDatabase.getInstance().getReference("usuarios/${user?.uid}")
                                    userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                            val nombreUsuario = dataSnapshot.child("nombre").getValue(String::class.java)
                                            val mensajeBienvenida = "Bienvenido, $nombreUsuario!"
                                            Toast.makeText(this@LoginActivity, mensajeBienvenida, Toast.LENGTH_LONG).show()
                                            guardarIdentificadorUsuario(dataSnapshot)
                                            val intent = Intent(this@LoginActivity, ClienteActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }

                                        override fun onCancelled(databaseError: DatabaseError) {
                                            showMessageDialog("Error", "No se pudo obtener información de usuario.")
                                        }
                                    })
                                } else if (tipoUsuario == "admin") {
                                    val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else if (tipoUsuario == "developer") {
                                    val intent = Intent(this@LoginActivity, DevActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    showMessageDialog("Error", "Tipo de usuario no válido.")
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                showMessageDialog("Error", "No se pudo obtener información de usuario.")
                            }
                        })
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


    //Con este identificador se puede consultar
    //el usuario en la base de datos con su hash
    private fun guardarIdentificadorUsuario(data: DataSnapshot) {
        // Obtener referencia a SharedPreferences
        val prefs = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE)

// Guardar un valor en SharedPreferences
        val editor = prefs.edit()
        var hash=data.key
        editor.putString("hasUsuario", hash)
        editor.apply()
    }

    // Función para mostrar un diálogo con un mensaje personalizado
    private fun showMessageDialog(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        builder.show()
    }
}
