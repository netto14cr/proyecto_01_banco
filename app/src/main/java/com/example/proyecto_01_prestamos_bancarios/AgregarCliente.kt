package com.example.proyecto_01_prestamos_bancarios

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import enviarCorreo

class AgregarClienteActivity : AppCompatActivity() {

    private lateinit var cedulaEditText: EditText
    private lateinit var nombreEditText: EditText
    private lateinit var salarioEditText: EditText
    private lateinit var telefonoEditText: EditText
    private lateinit var fechaEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var direccionEditText: EditText
    private lateinit var estadoCivilRadioGroup: RadioGroup
    private lateinit var agregarButton: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_cliente)

        cedulaEditText = findViewById(R.id.cedula_edit_text)
        nombreEditText = findViewById(R.id.nombre_edit_text)
        salarioEditText = findViewById(R.id.salario_edit_text)
        telefonoEditText = findViewById(R.id.telefono_edit_text)
        fechaEditText = findViewById(R.id.fecha_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)
        direccionEditText = findViewById(R.id.direccion_edit_text)
        estadoCivilRadioGroup = findViewById(R.id.estado_civil_radio_group)
        agregarButton = findViewById(R.id.boton_agregar)

        // Inicializar Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Inicializar Firebase Realtime Database
        database = FirebaseDatabase.getInstance().reference

        agregarButton.setOnClickListener {
            val cedula = cedulaEditText.text.toString()
            val nombre = nombreEditText.text.toString()
            val salario = salarioEditText.text.toString()
            val telefono = telefonoEditText.text.toString()
            val fecha = fechaEditText.text.toString()

            val direccion = direccionEditText.text.toString()
            val estadoCivil = when (estadoCivilRadioGroup.checkedRadioButtonId) {
                R.id.soltero_radio_button -> "Soltero"
                R.id.casado_radio_button -> "Casado"
                R.id.divorciado_radio_button -> "Divorciado"
                else -> "Soltero" // Por defecto se considera soltero si no se ha seleccionado nada
            }

            // Validar que los campos no estén vacíos
            if (cedula.isBlank() || nombre.isBlank() || salario.isBlank() || telefono.isBlank() || fecha.isBlank() || direccion.isBlank()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear el usuario en Firebase Authentication
            val email = emailEditText.text.toString()
            val password = "20232024" // La contraseña del cliente se asigna por defecto
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val firebaseUser = auth.currentUser
                        val uid = firebaseUser?.uid

                        // Crear un objeto cliente con los datos
                        val nuevoCliente = hashMapOf(
                            "cedula" to cedula,
                            "nombre" to nombre,
                            "salario" to salario,
                            "fecha_nacimiento" to fecha,
                            "direccion" to direccion,
                            "estado_civil" to estadoCivil,
                            "ahorros" to hashMapOf(
                                "Navidad" to 0,
                                "Escolar" to 0,
                                "Marchamo" to 0,
                                "Extraordinario" to 0
                            )
                        )

                        // Crear un objeto usuario con los datos
                        val nuevoUsuario = hashMapOf(
                            "nombre" to nombre,
                            "tipo_usuario" to "Cliente",
                            "datos_adicionales" to hashMapOf(
                                "email" to email
                            )
                        )

                        // Guardar el objeto cliente en Firebase Realtime Database
                        if (uid != null) {
                            database.child("clientes").child(uid).setValue(nuevoCliente)
                                .addOnCompleteListener { clienteTask ->
                                    if (clienteTask.isSuccessful) {
                                        // Enviar correo electrónico con la información de registro
                                        //val subject = "Registro de cliente exitoso"
                                        //val message = "¡Hola $nombre! Tu registro como cliente ha sido exitoso. Tu cédula es $cedula y tu contraseña es $password. Gracias por confiar en nuestro servicio."
                                        //enviarCorreo(this,email, subject, message)

                                        // Mostrar cuadro de diálogo de confirmación al usuario
                                        val builder = AlertDialog.Builder(this)
                                        builder.setMessage("Se ha registrado la información del nuevo cliente correctamente")
                                            .setCancelable(false)
                                            .setPositiveButton("Aceptar") { dialog, _ ->
                                                dialog.dismiss()
                                                finish()
                                            }
                                        val alert = builder.create()
                                        alert.show()

                                        // Obtener el UID de usuario actualmente autenticado en Firebase Authentication
                                        val usuarioUID = FirebaseAuth.getInstance().currentUser?.uid

                                        // Guardar el objeto usuario en Firebase Realtime Database usando el mismo UID que se creó al crear el usuario en Firebase Authentication
                                        if (usuarioUID != null) {
                                            database.child("usuarios").child(usuarioUID).setValue(nuevoUsuario)
                                                .addOnCompleteListener { usuarioTask ->
                                                    if (usuarioTask.isSuccessful) {
                                                        Log.d(TAG, "Nuevo usuario agregado exitosamente")
                                                    } else {
                                                        Log.e(TAG, "Error al agregar el nuevo usuario", usuarioTask.exception)
                                                    }
                                                }
                                        } else {
                                            Log.e(TAG, "No se pudo obtener el UID de usuario actualmente autenticado en Firebase Authentication")
                                        }
                                    } else {
                                        // Mostrar mensaje de error al usuario
                                        val error = "Ha ocurrido un error al registrar al cliente"
                                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                                        Log.e(TAG, "Error al agregar el nuevo cliente", clienteTask.exception)
                                    }
                                }
                        }
                    } else {
                        // Mostrar mensaje de error al usuario
                        val error = "Ha ocurrido un error al registrar al cliente"
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "Error al crear el nuevo usuario", task.exception)
                    }
                }


        }

        findViewById<Button>(R.id.boton_cancelar).setOnClickListener {
            finish()
        }


    }

    companion object {
        private const val TAG = "AgregarClienteActivity"
    }
}

