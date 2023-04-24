package com.example.proyecto_01_prestamos_bancarios

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
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
                        val prestamosActivos = mutableListOf<Prestamo2>()
                        val prestamosInactivos = mutableListOf<Prestamo2>()
                        for (prestamoSnapshot in snapshot.children) {
                            val prestamo = prestamoSnapshot.getValue(Prestamo2::class.java)
                            prestamo?.let {
                                it.id = prestamoSnapshot.key ?: ""
                                if (it.prestamoActivo) {
                                    prestamosActivos.add(it)
                                } else {
                                    prestamosInactivos.add(it)
                                }
                            }
                        }

                        val prestamosList = (prestamosActivos + prestamosInactivos).toMutableList()
                        prestamosList.sortByDescending { it.fechaInicio }
                        val adapter = PrestamosAdapter(prestamosList,userId)
                        rvPrestamos.adapter = adapter
                        adapter.setOnItemClickListener(object : PrestamosAdapter.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int, prestamo: Prestamo2) {
                                // Abrir actividad para mostrar detalles del préstamo
                                // Enviar el objeto prestamo como argumento
                            }
                        })
                    } else {
                        val builder = AlertDialog.Builder(this@PrestamosActivity)
                        builder.setTitle("Información")
                        builder.setMessage("El cliente no tiene préstamos asociados.")
                        builder.setNegativeButton("Volver") { dialog, _ ->
                            dialog.dismiss()
                            onBackPressed()
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar error
                }
            })
        }
    }

}
