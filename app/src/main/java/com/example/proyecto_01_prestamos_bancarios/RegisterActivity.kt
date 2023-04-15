package com.example.proyecto_01_prestamos_bancarios

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var nombreEditText: EditText
    private lateinit var claveEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var registerButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        nombreEditText = findViewById(R.id.nombre_edit_text)
        claveEditText = findViewById(R.id.clave_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)
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

        // Inicializar Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Inicializar Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference("usuarios")

        registerButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val clave = claveEditText.text.toString()
            val email = emailEditText.text.toString()
            val tipoUsuario = tipoUsuarioSpinner.selectedItem.toString()

            // Validar que los campos no estén vacíos
            if (nombre.isBlank() || clave.isBlank() || email.isBlank()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear el usuario en Firebase Authentication
            auth.createUserWithEmailAndPassword(email, clave)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val firebaseUser = auth.currentUser
                        val uid = firebaseUser?.uid

                        // Crear un objeto usuario con los datos
                        val nuevoUsuario = hashMapOf(
                            "nombre" to nombre,
                            "tipo_usuario" to tipoUsuario,
                            "datos_adicionales" to hashMapOf(
                                "email" to email
                            )
                        )

                        // Agregar el usuario a la base de datos
                        if (uid != null) {
                            database.child(uid).setValue(nuevoUsuario)
                                .addOnSuccessListener {
                                    Log.d(TAG, "Usuario agregado con ID: $uid")
                                    val alertDialog = AlertDialog.Builder(this)
                                        .setTitle("Registro exitoso")
                                        .setMessage("El usuario ha sido registrado exitosamente.")
                                        .setPositiveButton("OK", null)
                                        .create()
                                    alertDialog.show()

                                    // Agregar un delay antes de redireccionar
                                    Handler(Looper.getMainLooper()).postDelayed({
                                        finish()
                                    }, 5000) //
                                }
                                .addOnFailureListener {
                                    Log.w(TAG, "Error al agregar usuario", it)
                                    Toast.makeText(
                                        this,
                                        "Error al agregar usuario",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                        } else {
                            Log.w(TAG, "UID es nulo")
                            Toast.makeText(this, "Error al agregar usuario", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            this,
                            "Error al registrar usuario: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}

