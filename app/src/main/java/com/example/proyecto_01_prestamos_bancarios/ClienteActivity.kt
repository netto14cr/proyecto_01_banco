package com.example.proyecto_01_prestamos_bancarios

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class ClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cliente)

        // Este es la opcion en pantalla de cliente para ver sus prestamos
        findViewById<View>(R.id.card_view_prestamos).setOnClickListener {
            // Abrir la actividad para ver los préstamos del cliente
            val intent = Intent(this@ClienteActivity, PrestamosActivity::class.java)
            startActivity(intent)
            // Mostrar un mensaje al usuario de que se abrió la actividad para ver los préstamos
            Toast.makeText(this@ClienteActivity, "Ver mis préstamos", Toast.LENGTH_SHORT).show()
        }
        // Este es la opcion en pantalla de cliente para gestionar sus ahorros
        findViewById<View>(R.id.card_view_ahorros).setOnClickListener {
            Toast.makeText(
                this@ClienteActivity,
                "Gestionar mis ahorros",
                Toast.LENGTH_SHORT
            ).show()
        }
        // Este es la opcion en pantalla de cliente para calcular la cuota
        findViewById<View>(R.id.card_view_cuota).setOnClickListener {
            Toast.makeText(
                this@ClienteActivity,
                "Calcular cuota",
                Toast.LENGTH_SHORT
            ).show()
        }
        // Este es la opcion en pantalla de cliente para cerrar sesion
        findViewById<View>(R.id.card_view_cerrar_sesion).setOnClickListener {
            // Borrar los datos de sesión guardados
            val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            // Mostrar un mensaje al usuario
            Toast.makeText(
                this@ClienteActivity,
                "La sesión se ha cerrado correctamente",
                Toast.LENGTH_SHORT
            ).show()

            // Redirigir al usuario a la pantalla de inicio de sesión
            val intent = Intent(this@ClienteActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
