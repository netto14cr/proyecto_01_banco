package com.example.proyecto_01_prestamos_bancarios

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class PrestamosAdapter(private val prestamosList: List<Prestamo2>, private val clienteId: String) :
    RecyclerView.Adapter<PrestamosAdapter.ViewHolder>() {

    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_prestamo, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = prestamosList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = prestamosList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(prestamo: Prestamo2) {
            itemView.findViewById<TextView>(R.id.tvMontoSolicitado).text = "Monto solicitado: " + prestamo.montoSolicitado.toString()
            itemView.findViewById<TextView>(R.id.tvPlazo).text = "Plazo: " + prestamo.plazoTexto.toString()
            itemView.findViewById<TextView>(R.id.tvTasa).text = "Tasa de interés: " + prestamo.tasaInteres.toString()
            itemView.findViewById<TextView>(R.id.tvMontoCuota).text = "Cuota: " + prestamo.montoCuota.toString()
            itemView.findViewById<TextView>(R.id.tvMontoPrestamo).text = "Monto real: " + prestamo.montoPrestamo.toString()
            itemView.findViewById<TextView>(R.id.tvSaldoPrestamo).text = "Saldo: " + String.format("%.2f", prestamo.saldo)
            itemView.findViewById<TextView>(R.id.tvTipoPrestamo).text = "Tipo: " + prestamo.tipoPrestamo
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaCreacion = prestamo.fechaInicio?.let { formatter.format(it) }
            itemView.findViewById<TextView>(R.id.tvFechaCreacion).text = "Fecha inicio: $fechaCreacion"
            val fechaFinalizacion = prestamo.fechaFinalizacion?.let { formatter.format(it) }
            itemView.findViewById<TextView>(R.id.tvFechaFinalizacion).text = "Fecha finalizacion: $fechaFinalizacion"

            // Establecer la visibilidad del ImageView según el estado del préstamo
            val ivEstadoPrestamo = itemView.findViewById<ImageView>(R.id.ivEstadoPrestamo)
            // Establecer la habilitación del botón según el estado del préstamo
            val btnPagarCuota = itemView.findViewById<Button>(R.id.btnPagarCuota)
            if (prestamo.prestamoActivo) {
                ivEstadoPrestamo.setImageResource(R.mipmap.check_verde)
                ivEstadoPrestamo.visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.tvEstadoPrestamo).text = "Estado: Activo"
                btnPagarCuota.isEnabled = true
            } else {
                ivEstadoPrestamo.setImageResource(R.mipmap.check_rojo)
                ivEstadoPrestamo.visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.tvEstadoPrestamo).text = "Estado: Completo"
                btnPagarCuota.isEnabled = false
            }


            // Setear onClickListener al itemView
            itemView.setOnClickListener {
                listener?.onItemClick(itemView, adapterPosition, prestamo)
            }

            // Setear onClickListener al botón
            itemView.findViewById<Button>(R.id.btnPagarCuota).setOnClickListener {
                val prestamoRef = FirebaseDatabase.getInstance().getReference("clientes/${clienteId}/prestamos/${prestamo.id}")
                prestamoRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val prestamo = snapshot.getValue(Prestamo2::class.java)
                        if (prestamo != null) {

                            // Obtener la referencia del cliente correspondiente
                            val clienteRef = FirebaseDatabase.getInstance().getReference("clientes/${clienteId}")
                            clienteRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(clienteSnapshot: DataSnapshot) {
                                    val cliente = clienteSnapshot.getValue(Cliente::class.java)
                                    if (cliente != null) {
                                        val builder = AlertDialog.Builder(itemView.context)
                                        val inflater = LayoutInflater.from(itemView.context)
                                        val dialogView = inflater.inflate(R.layout.dialog_pagar_cuotas, null)
                                        builder.setView(dialogView)
                                        builder.setTitle("Pagar cuotas del préstamo")


                                        val cuotasRestantes = prestamo.cuotasTotales - prestamo.cuotasCanceladas
                            val cuotasArray = (1..cuotasRestantes).toList().toTypedArray()
                            val cuotasSpinner = dialogView.findViewById<Spinner>(R.id.spinnerCuotas)
                            val cuotasAdapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_item, cuotasArray)
                            cuotasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            cuotasSpinner.adapter = cuotasAdapter

                            val montoCuotaTextView = dialogView.findViewById<TextView>(R.id.tvMontoCuota)
                            montoCuotaTextView.text = "Valor de cuota: ${String.format("%.2f", prestamo.montoCuota)}"
                            val montoTotalTextView = dialogView.findViewById<TextView>(R.id.tvMontoTotal)
                            cuotasSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                                    val cuotasPagadas = cuotasSpinner.selectedItem.toString().toInt()
                                    val montoTotalPagado = prestamo.montoCuota * cuotasPagadas

                                    montoCuotaTextView.text = "Monto de cuota: ${prestamo.montoCuota}"
                                    montoTotalTextView.text = "Monto total a pagar: ${String.format("%.2f", montoTotalPagado)}"
                                    // Validar el salario del cliente
                                    if (cliente.salario?.toDoubleOrNull() ?: 0.0 < montoTotalPagado) {
                                        montoTotalTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                                        Toast.makeText(itemView.context, "El salario del cliente no es suficiente para realizar el pago", Toast.LENGTH_SHORT).show()
                                    } else {
                                        montoTotalTextView.setTextColor(ContextCompat.getColor(itemView.context, R.color.black))
                                        }
                                }
                                override fun onNothingSelected(parent: AdapterView<*>) {
                                    // Do nothing
                                }
                            }

                            builder.setPositiveButton("Pagar") { _, _ ->
                                val cuotasPagadas = cuotasSpinner.selectedItem.toString().toInt()
                                val montoTotalPagado = prestamo.montoCuota * cuotasPagadas
                                val saldoActual = cliente.salario?.toDoubleOrNull()?.minus(montoTotalPagado) ?: 0.0

                                if (cliente.salario?.toDoubleOrNull() ?: 0.0 < montoTotalPagado) {
                                    val builder = AlertDialog.Builder(itemView.context)
                                    builder.setTitle("Error de pago")
                                    builder.setMessage("El salario del cliente no es suficiente para realizar el pago")
                                    builder.setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
                                    val dialog = builder.create()
                                    dialog.show()

                                } else {
                                    // Realizar pago y actualizar datos en la base de datos

                                    // Actualizar el saldo del préstamo
                                    val saldoAnterior = prestamo.saldo
                                    prestamo.saldo = String.format("%.2f", prestamo.saldo - montoTotalPagado).toDouble()
                                    prestamo.cuotasCanceladas += cuotasPagadas

                                    if (prestamo.saldo <= 0) {
                                        prestamo.prestamoActivo = false
                                    }

                                    if (prestamo.cuotasCanceladas == prestamo.cuotasTotales) {
                                        prestamo.saldo = 0.0
                                        prestamo.prestamoActivo = false
                                    }

                                    // Actualizar el saldo del cliente
                                    val salarioUpdates = HashMap<String, Any>()
                                    salarioUpdates["salario"] = String.format("%.2f", saldoActual)
                                    clienteRef.updateChildren(salarioUpdates)


                                    val prestamoUpdates = HashMap<String, Any>()
                                    prestamoUpdates["saldo"] = prestamo.saldo
                                    prestamoUpdates["cuotasCanceladas"] = prestamo.cuotasCanceladas
                                    prestamoUpdates["prestamoActivo"] = prestamo.prestamoActivo
                                    prestamoRef.updateChildren(prestamoUpdates)

                                    val fechaPago = Date()
                                    val pagoRef =
                                        FirebaseDatabase.getInstance().getReference("pagos").push()
                                    val pago = hashMapOf(
                                        "cuotasPagadas" to cuotasPagadas,
                                        "montoPagado" to montoTotalPagado,
                                        "saldoAnterior" to saldoAnterior,
                                        "saldoActual" to prestamo.saldo,
                                        "fechaPago" to fechaPago
                                    )
                                    pagoRef.setValue(pago).addOnSuccessListener {
                                        // Agregar el ID del pago al nodo de pagos del prestamo
                                        val pagoId = pagoRef.key
                                        if (pagoId != null) {
                                            prestamoRef.child("pagos").child(pagoId).setValue(true)
                                        }
                                    }.addOnFailureListener { e ->
                                        // Handle error
                                        Toast.makeText(
                                            itemView.context,
                                            "Error al realizar el pago",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }


                                    val builder2 = AlertDialog.Builder(itemView.context)
                                    builder2.setTitle("Pago realizado")
                                    builder2.setMessage("Se ha realizado el pago de $${montoTotalPagado} correspondiente a $cuotasPagadas cuotas.")
                                    builder2.setPositiveButton("OK") { _, _ -> }
                                    val dialog2 = builder2.create()
                                    dialog2.show()
                                }
                            }

                            builder.setNegativeButton("Cancelar") { _, _ -> }
                            val dialog = builder.create()
                            dialog.show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Error al obtener los datos del cliente", error.toException())
                    }
                })
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Error al obtener el préstamo del cliente", error.toException())
                    }
                })
            }

        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, prestamo: Prestamo2)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}
