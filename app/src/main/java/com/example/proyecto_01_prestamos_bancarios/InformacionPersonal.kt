package com.example.proyecto_01_prestamos_bancarios

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class InformacionPersonal : AppCompatActivity() {


    private lateinit var btnModificar: Button
    private lateinit var editTextNombre:EditText
    private lateinit var editTextSalario:EditText
    private lateinit var editTextTelefono:EditText
    private lateinit var editTextFechaNacimiento:EditText

    private lateinit var radioButtonSoltero:RadioButton
    private lateinit var radioButtonCasado:RadioButton
    private lateinit var radioButtonDivorciado:RadioButton
    private lateinit var radioButtonViudo:RadioButton
    private lateinit var radioButtonGrupoEstadoCivil:RadioGroup
    private lateinit var estado_civil:String


    private lateinit var editTexDireccion:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informacion_personal)

        asignandoVariablesVista()
        btnModificar.setOnClickListener { modificarDatos() }
        cargandoVariablesVista()

    }

    fun cargandoVariablesVista(){
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("clientes")

        //obtener el usuario logueado
        // Obtener referencia a SharedPreferences
        val prefs = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE)

        val hashUsuario = prefs.getString("hasUsuario", "0")

        val userRef = database.getReference("usuarios/$hashUsuario")
        val userRef2 = database.getReference("clientes/$hashUsuario")

        val a=1
        //clientes
        if (hashUsuario != null) {
            myRef.child(hashUsuario).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val nombre = snapshot.child("nombre").getValue(String::class.java)
                        val salario = snapshot.child("salario").getValue(String::class.java)
                        val telefono = snapshot.child("telefono").getValue(String::class.java)
                        val fechaNac = snapshot.child("fecha_nacimiento").getValue(String::class.java)
                        val estadoCivil = snapshot.child("estado_civil").getValue(String::class.java)
                        val direccion = snapshot.child("direccion").getValue(String::class.java)
                        val cedula = snapshot.child("cedula").getValue(String::class.java)

                        if(estadoCivil=="Soltero") {
                            radioButtonSoltero.isChecked = true
                            estado_civil="Soltero"
                        }else if(estadoCivil=="Divorciado"){
                            radioButtonDivorciado.isChecked=true
                            estado_civil="Divorciado"
                        }else if(estadoCivil=="Viudo"){
                            radioButtonViudo.isChecked=true
                            estado_civil="Viudo"
                        }else if(estadoCivil=="Casado"){
                            radioButtonCasado.isChecked=true
                            estado_civil="Casado"
                        }else{
                            radioButtonGrupoEstadoCivil.clearCheck()
                            estado_civil=""
                        }

                        editTextNombre.setText(nombre)
                        editTextSalario.setText(salario)
                        editTextTelefono.setText(telefono)
                        editTextFechaNacimiento.setText(fechaNac)
                        editTexDireccion.setText(direccion)


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
    fun asignandoVariablesVista(){
        btnModificar=findViewById<Button?>(R.id.btnModificarInfoP)
        editTextNombre=findViewById<EditText?>(R.id.editTextNombreInfoP)
        editTextSalario=findViewById<EditText?>(R.id.editTextSalarioInfoP)
        editTextTelefono=findViewById<EditText?>(R.id.editTextTelefonoInfoP)
        editTextFechaNacimiento=findViewById<EditText?>(R.id.editTextFechNacInfP)
        radioButtonSoltero=findViewById(R.id.radioButtonSoltInfoP)
        radioButtonCasado=findViewById(R.id.radioButtonCasadoInfoP)
        radioButtonDivorciado=findViewById(R.id.radioButtonDivorciInfoP)
        radioButtonViudo=findViewById(R.id.radioButtonViudoInfoP)
        radioButtonGrupoEstadoCivil=findViewById(R.id.radioGroupEstadoCivilInfoP)
        editTexDireccion=findViewById<EditText?>(R.id.editTextTextDereccionInfoP)
    }

    @SuppressLint("SuspiciousIndentation")
    fun modificarDatos(){
        //obtener el usuario logueado
        // Obtener referencia a SharedPreferences
        val prefs = getSharedPreferences("infoUsuario", Context.MODE_PRIVATE)

        val hashUsuario = prefs.getString("hasUsuario", "0")


            val database = FirebaseDatabase.getInstance()
            val myRef = hashUsuario?.let { it1 -> database.getReference("clientes").child(it1) }

        val selectedRadioButtonId = radioButtonGrupoEstadoCivil.checkedRadioButtonId
        var esta_strin = estado_civil
        if (selectedRadioButtonId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
            val radioButtonValue = selectedRadioButton.text.toString()
            esta_strin=radioButtonValue
        }

        val nuevosValores = mapOf<String, Any>(
                "nombre" to editTextNombre.text.toString(),
                "salario" to editTextSalario.text.toString(),
                "fecha_nacimiento" to editTextFechaNacimiento.text.toString(),
                "estado_civil" to esta_strin,
                "direccion" to editTexDireccion.text.toString()
            )
            if (hashUsuario != null) {
                if (myRef != null) {
                    myRef.updateChildren(nuevosValores)
                }
            }

        Toast.makeText(this, "Datos actualizados!", Toast.LENGTH_SHORT).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@InformacionPersonal, ClienteActivity::class.java)
        startActivity(intent)
    }

}