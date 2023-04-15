package com.example.proyecto_01_prestamos_bancarios

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlin.math.pow

class AsignarPrestamoActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var cliente: Cliente
    private lateinit var spTipoPrestamo: Spinner
    private lateinit var etMontoPrestamo: EditText
    private lateinit var spPlazo: Spinner
    private lateinit var tvMontoCuota: TextView
    private lateinit var tvNombre: EditText
    private lateinit var tvSalario: EditText
    private lateinit var btnAsignar: Button
    private lateinit var  spCedulas: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignar_prestamo)

        // Obtiene una referencia a la base de datos
        database = FirebaseDatabase.getInstance().reference
        spTipoPrestamo = findViewById(R.id.spTipoPrestamo)
        etMontoPrestamo = findViewById(R.id.etMontoPrestamo)
        spPlazo = findViewById(R.id.spPlazo)
        tvMontoCuota = findViewById(R.id.tvMontoCuota)
        tvNombre = findViewById(R.id.etNombre)
        tvSalario = findViewById(R.id.etSalario)
        btnAsignar = findViewById(R.id.btnAsignar)
        spCedulas = findViewById(R.id.spCedulas)

        // Crea la lista de cédulas para mostrar en el Spinner
        val cedulasList = mutableListOf("Seleccionar cliente")
        val query = database.child("clientes").orderByChild("cedula")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (clienteSnapshot in snapshot.children) {
                        val cedula = clienteSnapshot.child("cedula").value as String
                        cedulasList.add(cedula)
                    }
                    // Crea un adaptador para el Spinner y le asigna la lista de cédulas
                    val adapter = ArrayAdapter(this@AsignarPrestamoActivity, android.R.layout.simple_spinner_item, cedulasList)
                    spCedulas.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al buscar cédulas: ${error.message}")
            }
        })

// Escucha los cambios en el valor del Spinner
        spCedulas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0) {
                    tvNombre.setText("")
                    tvSalario.setText("")
                    return
                }
                val cedula = parent.getItemAtPosition(position) as String
                val query = database.child("clientes").orderByChild("cedula").equalTo(cedula)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (clienteSnapshot in snapshot.children) {
                                val cliente = clienteSnapshot.getValue(Cliente::class.java)
                                val nombre = cliente?.nombre ?: ""
                                val salario = cliente?.salario ?: ""
                                tvNombre.setText(nombre)
                                tvSalario.setText(salario)
                                cliente?.cedula = cedula
                                Toast.makeText(this@AsignarPrestamoActivity, "Datos encontrados", Toast.LENGTH_SHORT).show()
                                break
                            }
                        } else {
                            tvNombre.setText("")
                            tvSalario.setText("")
                            val alertDialog = AlertDialog.Builder(this@AsignarPrestamoActivity)
                            alertDialog.setTitle("Error")
                            alertDialog.setMessage("No se encontraron datos para la cédula ingresada")
                            alertDialog.setPositiveButton("OK", null)
                            alertDialog.show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Firebase", "Error al buscar cliente: ${error.message}")
                    }
                })
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                parent?.getChildAt(0)?.isEnabled = false
            }
        }


        // Asigna el préstamo
        btnAsignar.setOnClickListener {
            val tipoPrestamo = spTipoPrestamo.selectedItem.toString()
            val montoPrestamo = etMontoPrestamo.text.toString().toDouble()
            val plazo = spPlazo.selectedItem.toString().toInt()
            val interes = obtenerInteres(tipoPrestamo)
            val montoCuota = calcularMontoCuota(montoPrestamo, plazo, interes)

            // Agrega el préstamo al cliente
            val prestamo = Prestamo(tipoPrestamo, montoPrestamo, interes, plazo, montoCuota)
            cliente.agregarPrestamo(prestamo)

            // Actualiza los datos del cliente en la base de datos
            database.child("clientes").child(cliente.cedula).setValue(cliente)

            // Muestra el monto de la cuota al usuario
            tvMontoCuota.text = "$${montoCuota}"
        }
    }

    private fun obtenerInteres(tipoPrestamo: String): Double {
        return when (tipoPrestamo) {
            "personal" -> 0.10
            "hipotecario" -> 0.08
            "vehicular" -> 0.12
            else -> throw IllegalArgumentException("Tipo de préstamo no válido")
        }
    }

    private fun calcularMontoCuota(montoPrestamo: Double, plazo: Int, interes: Double): Double {
        val r = interes / 12
        val n = plazo * 12
        val cuota = (montoPrestamo * r) / (1 - (1 + r).pow(-n))
        return cuota
    }

}