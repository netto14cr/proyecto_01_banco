package com.example.proyecto_01_prestamos_bancarios

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
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

    private lateinit var editTextNavidad: EditText
    private lateinit var editTextEscolar: EditText
    private lateinit var editTextMarchamo: EditText
    private lateinit var editTextExtraordinario: EditText

    fun inicializarVariables(){
        btnNavidad=findViewById(R.id.buttonNavidad)
        btnEscolar=findViewById(R.id.buttonEscolar)
        btnMarchamo=findViewById(R.id.buttonMarchamo)
        btnExtraordinario=findViewById(R.id.buttonExtraordinario)


        editTextNavidad=findViewById(R.id.editTextCuotaNavidad)
        editTextEscolar=findViewById(R.id.editTextCuotaEscolar)
        editTextMarchamo=findViewById(R.id.editTextCuotaMarchamo)
        editTextExtraordinario=findViewById(R.id.editTextCuotaExtraordinario)


        btnNavidad.setOnClickListener{
            // obtenemos el valor del editText y si es mayor a 5000
            // entonces modificamos el valor en la base de datos y refrescamos la actividad.

        }

    }

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
                        val ahorroNavidad = ahorros?.get("Navidad") as Long
                        val ahorroEscolar = ahorros?.get("Escolar") as Long
                        val ahorroMarchamo = ahorros?.get("Marchamo") as Long
                        val ahorroExtraordinario = ahorros?.get("Extraordinario") as Long

                        editTextNavidad.setText(ahorroNavidad.toString())
                        editTextEscolar.setText(ahorroEscolar.toString())
                        editTextMarchamo.setText(ahorroMarchamo.toString())
                        editTextExtraordinario.setText(ahorroExtraordinario.toString())


                        if (ahorroNavidad<5000) {
                            btnNavidad.setBackgroundColor(Color.RED)
                        } else {
                            btnNavidad.setBackgroundColor(Color.GREEN)
                        }
                        if (ahorroEscolar<5000) {
                            btnEscolar.setBackgroundColor(Color.RED)
                        } else {
                            btnEscolar.setBackgroundColor(Color.GREEN)
                        }
                        if (ahorroMarchamo<5000) {
                            btnMarchamo.setBackgroundColor(Color.RED)
                        } else {
                            btnMarchamo.setBackgroundColor(Color.GREEN)
                        }
                        if (ahorroExtraordinario<5000) {
                            btnExtraordinario.setBackgroundColor(Color.RED)
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
       /* // Update the document with a new field
        val addOnFailureListener = myRef.update("newField", "newValue")
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener {Log.w(TAG, "Error updating document")
            }
*/

    }
}