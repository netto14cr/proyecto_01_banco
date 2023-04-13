package com.example.proyecto_01_prestamos_bancarios

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var claveEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var telefonoEditText: EditText
    private lateinit var registerButton: Button

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nombreEditText = findViewById(R.id.nombre_edit_text)
        claveEditText = findViewById(R.id.clave_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)
        telefonoEditText = findViewById(R.id.telefono_edit_text)
        val tipoUsuarioSpinner = findViewById<Spinner>(R.id.tipo_usuario_spinner)

        // Crea un ArrayAdapter usando el array de opciones y un layout de spinner por defecto
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.tipo_usuario_opciones,
            android.R.layout.simple_spinner_item
        )

        // Especifica el layout que se usará cuando la lista de opciones aparezca
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asigna el adaptador al spinner
        tipoUsuarioSpinner.adapter = adapter

        registerButton = findViewById(R.id.boton_registrar)

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance()

        registerButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val clave = claveEditText.text.toString()
            val email = emailEditText.text.toString()
            val telefono = telefonoEditText.text.toString()
            val tipoUsuario = tipoUsuarioSpinner.selectedItem.toString()

            // Validar que los campos no estén vacíos
            if (nombre.isBlank() || clave.isBlank() || email.isBlank() || telefono.isBlank()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear un objeto usuario con los datos
            val nuevoUsuario = hashMapOf(
                "nombre" to nombre,
                "clave" to clave,
                "tipo_usuario" to tipoUsuario,
                "datos_adicionales" to hashMapOf(
                    "email" to email,
                    "telefono" to telefono
                )
            )

            // Agregar el usuario a la base de datos
            db.collection("usuarios")
                .add(nuevoUsuario)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Usuario agregado con ID: ${documentReference.id}")
                    finish()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error al agregar el usuario", e)
                    Toast.makeText(this, "Error al agregar el usuario", Toast.LENGTH_SHORT).show()
                }
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}
