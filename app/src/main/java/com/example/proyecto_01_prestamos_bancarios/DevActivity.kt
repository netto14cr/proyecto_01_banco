package com.example.proyecto_01_prestamos_bancarios

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class DevActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev)

        // Este es la opcion en pantalla de developer para registrar un administrador
        findViewById<View>(R.id.card_view_registro_admin).setOnClickListener {
            // Abrir la actividad para agregar un nuevo administrador
            val intent = Intent(this@DevActivity, RegisterActivity::class.java)
            startActivity(intent)
            // Mostrar un mensaje al usuario de que se abrió la actividad para agregar un nuevo administrador
            Toast.makeText(this@DevActivity, "Registrar administrador", Toast.LENGTH_SHORT).show()
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
                this@DevActivity,
                "La sesión se ha cerrado correctamente",
                Toast.LENGTH_SHORT
            ).show()

            // Redirigir al usuario a la pantalla de inicio de sesión
            val intent = Intent(this@DevActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
