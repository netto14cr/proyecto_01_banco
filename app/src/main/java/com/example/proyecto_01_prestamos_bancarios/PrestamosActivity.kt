package com.example.proyecto_01_prestamos_bancarios

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PrestamosActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var rvPrestamos: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prestamos)

        rvPrestamos = findViewById(R.id.recyclerview)

        // Inicializar instancia de Firebase Auth y Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Configuración del RecyclerView
        rvPrestamos.layoutManager = LinearLayoutManager(this)

        // Obtener prestamos del usuario actual
        obtenerPrestamos()
    }

    private fun obtenerPrestamos() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val prestamosRef = database.child("clientes").child(userId).child("prestamos")
            prestamosRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val prestamosList = mutableListOf<Prestamo2>()
                        for (prestamoSnapshot in snapshot.children) {
                            val prestamo = prestamoSnapshot.getValue(Prestamo2::class.java)
                            prestamo?.let {
                                it.id = prestamoSnapshot.key ?: ""
                                prestamosList.add(it)
                            }
                        }
                        // Configuración del adaptador del RecyclerView
                        val adapter = PrestamosAdapter(prestamosList)
                        rvPrestamos.adapter = adapter

                        // Configuración de onClickListener para mostrar detalles del préstamo
                        adapter.setOnItemClickListener(object : PrestamosAdapter.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int, prestamo: Prestamo2) {
                                // Abrir actividad para mostrar detalles del préstamo
                                // Enviar el objeto prestamo como argumento
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar error
                }
            })
        }
    }
}
