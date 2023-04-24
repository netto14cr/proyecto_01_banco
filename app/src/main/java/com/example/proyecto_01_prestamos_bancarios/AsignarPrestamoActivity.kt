package com.example.proyecto_01_prestamos_bancarios

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.pow

class AsignarPrestamoActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var cliente: Cliente
    private lateinit var spTipoPrestamo: Spinner
    private lateinit var etMontoPrestamo: EditText
    private lateinit var spPlazo: Spinner
    private lateinit var tvMontoCuota: TextView
    private lateinit var tvFechaFinalizacion: TextView
    private lateinit var tvNombre: EditText
    private lateinit var tvSalario: EditText
    private lateinit var btnAsignar: Button
    private lateinit var spCedulas: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asignar_prestamo)

        // Obtiene una referencia a la base de datos
        database = FirebaseDatabase.getInstance().reference
        spTipoPrestamo = findViewById(R.id.spTipoPrestamo)
        etMontoPrestamo = findViewById(R.id.etMontoPrestamo)
        spPlazo = findViewById(R.id.spPlazo)
        tvMontoCuota = findViewById(R.id.tvMontoCuota)
        tvFechaFinalizacion = findViewById(R.id.tvFechaFinalizacion)
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
                    val adapter = ArrayAdapter(
                        this@AsignarPrestamoActivity,
                        android.R.layout.simple_spinner_item,
                        cedulasList
                    )
                    spCedulas.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al buscar cédulas: ${error.message}")
            }
        })

        // Escucha los cambios en el valor del Spinner
        spCedulas.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    tvNombre.setText("")
                    tvSalario.setText("")
                    return
                }
                // Obtiene la cédula seleccionada
                val cedula = parent.getItemAtPosition(position) as String
                // Busca el cliente con la cédula seleccionada
                val query = database.child("clientes").orderByChild("cedula").equalTo(cedula)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (clienteSnapshot in snapshot.children) {
                                cliente = clienteSnapshot.getValue(Cliente::class.java)!!
                                // Obtener el UID del cliente
                                cliente.id = clienteSnapshot.key!!
                                tvNombre.setText(cliente.nombre)
                                tvSalario.setText(cliente.salario.toString())
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("Firebase", "Error al buscar cliente: ${error.message}")
                    }
                })
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se hace nada
            }
        }

        // Escucha los cambios en el valor del Spinner de tipo de préstamo
        spTipoPrestamo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                calcularMontoCuota()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se hace nada
            }
        }

        // Escucha los cambios en el valor del EditText de monto de préstamo
        etMontoPrestamo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                calcularMontoCuota()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se hace nada
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se hace nada
            }
        })

        // Escucha los cambios en el valor del Spinner de plazo de préstamo
        spPlazo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                calcularMontoCuota()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se hace nada
            }
        }

        // Asigna el listener al botón para asignar el préstamo
        btnAsignar.setOnClickListener {
            asignarPrestamo()
        }
    }

    // Calcula el monto de la cuota mensual del préstamo
    private fun calcularMontoCuota() {
        val montoPrestamoStr = etMontoPrestamo.text.toString()
        if (isNumeric(montoPrestamoStr)) {
            val montoPrestamo = montoPrestamoStr.toDouble()
            val plazo = obtenerPlazoEnMeses()
            val tasaInteres = calcularTasaInteres(spTipoPrestamo.selectedItemPosition)
            val interesMensual = tasaInteres / 12
            val denominador = 1 - (1 + interesMensual).pow(-plazo)
            val montoCuota = montoPrestamo * interesMensual / denominador

            val clienteSalarioStr = cliente.salario
            if (isNumeric(clienteSalarioStr)) {
                val clienteSalario = clienteSalarioStr?.toDouble() ?: 0.0
                val maximoPermitido = clienteSalario * 0.45
                if (montoPrestamo > maximoPermitido) {
                    val mensaje = "El monto del préstamo no puede ser mayor al 45% del salario del cliente"
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                    btnAsignar.isEnabled = false
                } else if (montoCuota > clienteSalario * 0.3) {
                    val mensaje = "El monto de la cuota no puede ser mayor al 30% del salario del cliente"
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                    btnAsignar.isEnabled = false
                } else {
                    tvMontoCuota.text = String.format("%.2f", montoCuota)
                    btnAsignar.isEnabled = true
                    // Actualizar fecha de finalización
                    val fechaInicio = Date()
                    val fechaFinalizacion = calcularFechaFinalizacion(fechaInicio, plazo)
                    tvFechaFinalizacion.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(fechaFinalizacion)
                }
            } else {
                tvMontoCuota.text = "El salario del cliente no es un número válido"
                btnAsignar.isEnabled = false
            }
        } else {
            tvMontoCuota.text = "Ingrese un monto válido"
            btnAsignar.isEnabled = false
        }
    }

    private fun calcularTasaInteres(tipoPrestamo: Int): Double {
        return when (tipoPrestamo) {
            0 -> 0.075 // Hipotecario
            1 -> 0.08 // Educación
            2 -> 0.1 // Personal
            3 -> 0.12 // Viajes
            else -> throw IllegalArgumentException("Tipo de préstamo desconocido: $tipoPrestamo")
        }
    }


    // Función para validar si un String es numérico
    private fun isNumeric(str: String?): Boolean {
        return str?.toDoubleOrNull() != null
    }

    private fun calcularFechaFinalizacion(fechaInicio: Date, plazo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.time = fechaInicio
        calendar.add(Calendar.MONTH, plazo)
        return calendar.time
    }

    private fun obtenerPlazoEnMeses(): Int {
        return when (spPlazo.selectedItemPosition) {
            0 -> 36
            1 -> 60
            2 -> 120
            else -> 0
        }
    }

    // Asigna el préstamo al cliente
    private fun asignarPrestamo() {
        // Verifica que se haya seleccionado un cliente
        if (tvNombre.text.toString().isEmpty()) {
            Toast.makeText(this, "Debe seleccionar un cliente", Toast.LENGTH_SHORT).show()
            return
        }

        // Crea el objeto préstamo con los datos ingresados
        val tipoPrestamo = spTipoPrestamo.selectedItem.toString()
        val montoPrestamo = etMontoPrestamo.text.toString().toDoubleOrNull() ?: return
        val plazo = obtenerPlazoEnMeses()
        val plazoTexto = spPlazo.selectedItem.toString()
        val tasaInteres = calcularTasaInteres(spTipoPrestamo.selectedItemPosition)
        val interesMensual = tasaInteres / 12
        val montoCuota = montoPrestamo * interesMensual / (1 - (1 + interesMensual).pow(-plazo))
        val montoSolicitado = montoPrestamo
        val montoPrestamo2 = (montoCuota * plazo)

        // Calcula las fechas de creación y finalización del préstamo
        val fechaInicio = Date()
        val fechaFinalizacion = calcularFechaFinalizacion(fechaInicio, plazo)

        // Crea un objeto Prestamo con los datos ingresados y las fechas calculadas
        val prestamo = Prestamo(
            tipoPrestamo = tipoPrestamo,
            montoPrestamo = String.format("%.2f", montoPrestamo2).toDouble(),
            montoSolicitado = montoSolicitado,
            saldo = String.format("%.2f", montoPrestamo2).toDouble(),
            plazoTexto = plazoTexto,
            tasaInteres = tasaInteres,
            montoCuota = String.format("%.2f", montoCuota).toDouble(),
            fechaInicio = fechaInicio,
            fechaFinalizacion = fechaFinalizacion,
            prestamoActivo = true,
            cuotasTotales = plazo,
            cuotasCanceladas = 0
        )

        // Obtiene el ID del cliente seleccionado
        val clienteId = cliente.id
        // Agrega el préstamo al cliente
        val prestamoRef = Firebase.database.getReference("clientes/$clienteId/prestamos").push()
        prestamoRef.setValue(prestamo).addOnSuccessListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Préstamo asignado correctamente")
            builder.setMessage("El préstamo ha sido asignado exitosamente.")
            builder.setPositiveButton("Aceptar") { dialog, which ->
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                finish()
            }
            val dialog = builder.create()
            dialog.show()
        }.addOnFailureListener { e ->
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Error al asignar préstamo")
            builder.setMessage("Ha ocurrido un error al asignar el préstamo: ${e.message}")
            builder.setPositiveButton("Aceptar", null)
            val dialog = builder.create()
            dialog.show()
            Log.e(TAG, "Error al asignar préstamo", e)
        }
    }


}

