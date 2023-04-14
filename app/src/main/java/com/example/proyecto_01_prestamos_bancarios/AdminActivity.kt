package com.example.proyecto_01_prestamos_bancarios

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        // Este es la opcion en pantalla de administrador para agregar un nuevo cliente
        findViewById<View>(R.id.card_view_agregar_cliente).setOnClickListener {
            // Agregar aquí la lógica para agregar un nuevo cliente
            Toast.makeText(
                this@AdminActivity,
                "Agregar nuevo cliente",
                Toast.LENGTH_SHORT
            ).show()
        }
        // Este es la opcion en pantalla de administrador para asignar un préstamo a un cliente
        findViewById<View>(R.id.card_view_asignar_prestamo).setOnClickListener {
            // Agregar aquí la lógica para asignar un préstamo a un cliente existente
            Toast.makeText(
                this@AdminActivity,
                "Asignar préstamo a cliente",
                Toast.LENGTH_SHORT
            ).show()
        }
        // Este es la opcion en pantalla de administrador para cerrar sesión
        findViewById<View>(R.id.card_view_cerrar_sesion_admin).setOnClickListener {
            // Borrar los datos de sesión guardados
            val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            // Mostrar un mensaje al usuario
            Toast.makeText(
                this@AdminActivity,
                "La sesión se ha cerrado correctamente",
                Toast.LENGTH_SHORT
            ).show()

            // Redirigir al usuario a la pantalla de inicio de sesión
            val intent = Intent(this@AdminActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
