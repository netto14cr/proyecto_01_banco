package com.example.proyecto_01_prestamos_bancarios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlin.math.ceil
import kotlin.math.pow


class CalcularCuota : AppCompatActivity() {

    private val hipotecarioPorcentajeInteres = 7.5
    private val educacionPorcentajeInteres = 8.0
    private val personalPorcentajeInteres = 10.0
    private val viajesPorcentajeInteres = 12.0

    private lateinit var calcularBoton :Button
    private lateinit var tipoCredito: String
    private lateinit var montoEditText: EditText
    private lateinit var plazos: String

    private lateinit var spinner:Spinner
    private lateinit var plazoSpinner:Spinner
    private lateinit var resultText:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calcular_cuota)

        resultText=findViewById(R.id.resultadoTextView)

        spinner=findViewById(R.id.tipoCreditoSpinner)
        montoEditText=findViewById(R.id.montoEditText)

        val opciones = resources.getStringArray(R.array.tipoCreditoOptions)

        spinner.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_item,opciones)


        plazoSpinner=findViewById(R.id.plazoSpinner)

        var opcionesPlazo=resources.getStringArray(R.array.plazoOptions)

        plazoSpinner.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_item,opcionesPlazo)

        calcularBoton=findViewById(R.id.calcularButton)
        tipoCredito=opciones[0]
        plazos=opcionesPlazo[0]
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(
                    applicationContext,
                    "Seleccionaste: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
                tipoCredito=selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se hace nada
            }
        }

        plazoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                Toast.makeText(
                    applicationContext,
                    "Seleccionaste: $selectedItem",
                    Toast.LENGTH_SHORT
                ).show()
                plazos=selectedItem
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No se hace nada
            }
        }

        val monto = montoEditText.text.toString().toInt()


        calcularBoton.setOnClickListener{
            val porcentajeInteres = when (tipoCredito) {
                "Hipotecario" -> hipotecarioPorcentajeInteres
                "Educación" -> educacionPorcentajeInteres
                "Personal" -> personalPorcentajeInteres
                "Viajes" -> viajesPorcentajeInteres
                else -> throw IllegalArgumentException("Tipo de crédito inválido")
            }
            val interesMensual = porcentajeInteres / 12 / 100
            val monto = montoEditText.text.toString().toInt()
            val cuotaMensual = monto * interesMensual / (1 - (1 + interesMensual).pow(plazos.substringBefore(" ").toInt() * -1))
            val cuotaRedondeada=ceil(cuotaMensual)

            resultText.setText("La cuota mensual a pagar es : " + cuotaRedondeada.toString() + " colones.")

        }
    }



}