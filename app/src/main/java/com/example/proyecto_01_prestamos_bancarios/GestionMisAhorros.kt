package com.example.proyecto_01_prestamos_bancarios

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.collection.ArraySortedMap
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class GestionMisAhorros : AppCompatActivity() {

    private lateinit var btnNavidad: Button
    private lateinit var btnEscolar: Button
    private lateinit var btnMarchamo: Button
    private lateinit var btnExtraordinario: Button

    private lateinit var btnEliminarNavidad: Button
    private lateinit var btnEliminarEscolar: Button
    private lateinit var btnEliminarMarchamo: Button
    private lateinit var btnEliminarExtraordinario: Button


    private lateinit var editTextNavidad: EditText
    private lateinit var editTextEscolar: EditText
    private lateinit var editTextMarchamo: EditText
    private lateinit var editTextExtraordinario: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_mis_ahorros)

        inicializarVariables()
        // empezar esta funcionalidad y terminarla mañana

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("clientes")

        //obtener el usuario logueado
        // Obtener referencia a SharedPreferences
        val prefs = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE)

        val hashUsuario = prefs.getString("hasUsuario", "0")

        val userRef = database.getReference("usuarios/$hashUsuario")
        val userRef2 = database.getReference("clientes/$hashUsuario")

        if (hashUsuario != null) {
            myRef.child(hashUsuario).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val nombre = snapshot.child("nombre").getValue(String::class.java)

                        val ahorrosSnapshot = snapshot.child("ahorros")
                        val ahorros = ahorrosSnapshot.getValue<HashMap<String, Any>>()
                        val ahorroNavidad = ahorros?.get("Navidad").toString().toInt()
                        val ahorroEscolar = ahorros?.get("Escolar").toString().toInt()
                        val ahorroMarchamo = ahorros?.get("Marchamo").toString().toInt()
                        val ahorroExtraordinario = ahorros?.get("Extraordinario").toString().toInt()

                        editTextNavidad.setText(ahorroNavidad.toString())
                        editTextEscolar.setText(ahorroEscolar.toString())
                        editTextMarchamo.setText(ahorroMarchamo.toString())
                        editTextExtraordinario.setText(ahorroExtraordinario.toString())


                        if (ahorroNavidad<5000) {
                            btnNavidad.setBackgroundColor(Color.RED)
                            editTextNavidad.setText("0")
                        } else {
                            btnNavidad.setBackgroundColor(Color.GREEN)
                        }
                        if (ahorroEscolar<5000) {
                            btnEscolar.setBackgroundColor(Color.RED)
                            editTextEscolar.setText("0")
                        } else {
                            btnEscolar.setBackgroundColor(Color.GREEN)
                        }
                        if (ahorroMarchamo<5000) {
                            btnMarchamo.setBackgroundColor(Color.RED)
                            editTextMarchamo.setText("0")
                        } else {
                            btnMarchamo.setBackgroundColor(Color.GREEN)
                        }
                        if (ahorroExtraordinario<5000) {
                            btnExtraordinario.setBackgroundColor(Color.RED)
                            editTextExtraordinario.setText("0")
                        } else {
                            btnExtraordinario.setBackgroundColor(Color.GREEN)
                        }

                        Log.d("Firebase", "El usuario es: $nombre")
                    } else {
                        Log.d("Firebase", "No se encontró el usuario")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Firebase", "Error al obtener el usuario: ${error.message}")
                }
            })
        }
    }
    fun inicializarVariables(){
        btnNavidad=findViewById(R.id.buttonNavidad)
        btnEscolar=findViewById(R.id.buttonEscolar)
        btnMarchamo=findViewById(R.id.buttonMarchamo)
        btnExtraordinario=findViewById(R.id.buttonExtraordinario)

        btnEliminarNavidad=findViewById(R.id.buttonEliminarNavidad)
        btnEliminarEscolar=findViewById(R.id.buttonEliminarEscolar)
        btnEliminarMarchamo=findViewById(R.id.buttonEliminarMarchamo)
        btnEliminarExtraordinario=findViewById(R.id.buttonEliminarExtraordinario)

        editTextNavidad=findViewById(R.id.editTextCuotaNavidad)
        editTextEscolar=findViewById(R.id.editTextCuotaEscolar)
        editTextMarchamo=findViewById(R.id.editTextCuotaMarchamo)
        editTextExtraordinario=findViewById(R.id.editTextCuotaExtraordinario)


        btnNavidad.setOnClickListener{
            // obtenemos el valor del editText y si es mayor a 5000
            // entonces modificamos el valor en la base de datos y refrescamos la actividad.
            modificarAhorros(editTextNavidad.text.toString())
        }

        btnEscolar.setOnClickListener{
            // obtenemos el valor del editText y si es mayor a 5000
            // entonces modificamos el valor en la base de datos y refrescamos la actividad.
            modificarAhorros(editTextEscolar.text.toString())
        }

        btnMarchamo.setOnClickListener{
            // obtenemos el valor del editText y si es mayor a 5000
            // entonces modificamos el valor en la base de datos y refrescamos la actividad.
            modificarAhorros(editTextMarchamo.text.toString())
        }

        btnExtraordinario.setOnClickListener{
            // obtenemos el valor del editText y si es mayor a 5000
            // entonces modificamos el valor en la base de datos y refrescamos la actividad.
            modificarAhorros(editTextExtraordinario.text.toString())
        }

        btnEliminarNavidad.setOnClickListener {
            eliminarAhorroNavidad()
        }

        btnEliminarEscolar.setOnClickListener {
            eliminarAhorroEscolar()
        }

        btnEliminarMarchamo.setOnClickListener {
            eliminarAhorroMarchamo()
        }

        btnEliminarExtraordinario.setOnClickListener {
            eliminarAhorroExtraordinario()
        }


    }

    private fun eliminarAhorroExtraordinario() {
        //obtener el usuario logueado
        // Obtener referencia a SharedPreferences
        val prefs = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE)
        val hashUsuario = prefs.getString("hasUsuario", "0")
        val database = FirebaseDatabase.getInstance()
        val myRef = hashUsuario?.let { it1 -> database.getReference("clientes").child(it1) }

        editTextExtraordinario.setText("0")

        // Solo se actuliza los valores de los ahorros.
        val nuevosValores = mapOf<String, Any>(
            "ahorros" to mapOf<String, Any>(
                "Navidad" to editTextNavidad.text.toString().toInt(),
                "Escolar" to editTextEscolar.text.toString().toInt(),
                "Marchamo" to editTextMarchamo.text.toString().toInt(),
                "Extraordinario" to 0
            )
        )
        if (hashUsuario != null) {
            if (myRef != null) {
                myRef.updateChildren(nuevosValores)
            }
        }
        this.recreate()
    }
    private fun eliminarAhorroMarchamo() {
        //obtener el usuario logueado
        // Obtener referencia a SharedPreferences
        val prefs = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE)
        val hashUsuario = prefs.getString("hasUsuario", "0")
        val database = FirebaseDatabase.getInstance()
        val myRef = hashUsuario?.let { it1 -> database.getReference("clientes").child(it1) }

        editTextMarchamo.setText("0")

        // Solo se actuliza los valores de los ahorros.
        val nuevosValores = mapOf<String, Any>(
            "ahorros" to mapOf<String, Any>(
                "Navidad" to editTextNavidad.text.toString().toInt(),
                "Escolar" to editTextEscolar.text.toString().toInt(),
                "Marchamo" to 0,
                "Extraordinario" to editTextExtraordinario.text.toString().toInt()
            )
        )
        if (hashUsuario != null) {
            if (myRef != null) {
                myRef.updateChildren(nuevosValores)
            }
        }
        this.recreate()
    }
    private fun eliminarAhorroEscolar() {
        //obtener el usuario logueado
        // Obtener referencia a SharedPreferences
        val prefs = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE)
        val hashUsuario = prefs.getString("hasUsuario", "0")
        val database = FirebaseDatabase.getInstance()
        val myRef = hashUsuario?.let { it1 -> database.getReference("clientes").child(it1) }

        editTextEscolar.setText("0")

        // Solo se actuliza los valores de los ahorros.
        val nuevosValores = mapOf<String, Any>(
            "ahorros" to mapOf<String, Any>(
                "Navidad" to editTextNavidad.text.toString().toInt(),
                "Escolar" to 0,
                "Marchamo" to editTextMarchamo.text.toString().toInt(),
                "Extraordinario" to editTextExtraordinario.text.toString().toInt()
            )
        )
        if (hashUsuario != null) {
            if (myRef != null) {
                myRef.updateChildren(nuevosValores)
            }
        }
        this.recreate()
    }
    private fun eliminarAhorroNavidad() {
        //obtener el usuario logueado
        // Obtener referencia a SharedPreferences
        val prefs = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE)
        val hashUsuario = prefs.getString("hasUsuario", "0")
        val database = FirebaseDatabase.getInstance()
        val myRef = hashUsuario?.let { it1 -> database.getReference("clientes").child(it1) }

            editTextNavidad.setText("0")

        // Solo se actuliza los valores de los ahorros.
        val nuevosValores = mapOf<String, Any>(
            "ahorros" to mapOf<String, Any>(
                "Navidad" to 0,
                "Escolar" to editTextEscolar.text.toString().toInt(),
                "Marchamo" to editTextMarchamo.text.toString().toInt(),
                "Extraordinario" to editTextExtraordinario.text.toString().toInt()
            )
        )
        if (hashUsuario != null) {
            if (myRef != null) {
                myRef.updateChildren(nuevosValores)
            }
        }
        this.recreate()
    }

    private fun modificarAhorros(valor:String) {

        if(valor.toString().toInt()>5000) {

            //obtener el usuario logueado
            // Obtener referencia a SharedPreferences
            val prefs = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE)

            val hashUsuario = prefs.getString("hasUsuario", "0")


            val database = FirebaseDatabase.getInstance()
            val myRef = hashUsuario?.let { it1 -> database.getReference("clientes").child(it1) }

            if(editTextNavidad.text.toString()===""){
                editTextNavidad.setText("0")
            }else if(editTextEscolar.text.toString()===""){
                editTextEscolar.setText("0")
            }else if(editTextMarchamo.text.toString()===""){
                editTextMarchamo.setText("0")
            }else if(editTextExtraordinario.text.toString()===""){
                editTextExtraordinario.setText("0")
            }
            // Solo se actuliza los valores de los ahorros.
            val nuevosValores = mapOf<String, Any>(
                "ahorros" to mapOf<String, Any>(
                    "Navidad" to editTextNavidad.text.toString().toInt(),
                    "Escolar" to editTextEscolar.text.toString().toInt(),
                    "Marchamo" to editTextMarchamo.text.toString().toInt(),
                    "Extraordinario" to editTextExtraordinario.text.toString().toInt()
                )
            )
            if (hashUsuario != null) {
                if (myRef != null) {
                    myRef.updateChildren(nuevosValores)
                }
            }
            this.recreate()
        }else{
            Toast.makeText(this,"El monto mensual debe ser mayor a 5.000 colones",Toast.LENGTH_SHORT).show()
            }

    }



}