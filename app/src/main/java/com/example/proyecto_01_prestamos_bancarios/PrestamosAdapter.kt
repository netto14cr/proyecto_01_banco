package com.example.proyecto_01_prestamos_bancarios

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class PrestamosAdapter(private val prestamosList: List<Prestamo2>) :
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
            itemView.findViewById<TextView>(R.id.tvMontoPrestamo).text = "Monto: " + prestamo.monto.toString()
            itemView.findViewById<TextView>(R.id.tvPlazo).text = "Plazo: " + prestamo.plazo.toString()
            itemView.findViewById<TextView>(R.id.tvTasa).text = "Tasa de interés: " + prestamo.tasaInteres.toString()
            itemView.findViewById<TextView>(R.id.tvMontoCuota).text = "Cuota: " + prestamo.montoCuota.toString()
            itemView.findViewById<TextView>(R.id.tvTipoPrestamo).text = "Tipo: " + prestamo.tipo.toString()
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val fechaCreacion = formatter.format(prestamo.fechaCreacion)
            itemView.findViewById<TextView>(R.id.tvFechaCreacion).text = "Fecha inicio: $fechaCreacion"
            val fechaFinalizacion = formatter.format(prestamo.fechaFinalizacion)
            itemView.findViewById<TextView>(R.id.tvFechaFinalizacion).text = "Fecha finalizacion: $fechaFinalizacion"


            // Setear onClickListener al itemView
            itemView.setOnClickListener {
                listener?.onItemClick(itemView, adapterPosition, prestamo)
            }

            // Setear onClickListener al botón
            itemView.findViewById<Button>(R.id.btnPagarCuota).setOnClickListener {
                // Aquí puedes llamar a una función que realice la acción de pagar la cuota correspondiente
                // por ejemplo, mostrar un diálogo de confirmación o redirigir al usuario a una pantalla de pago
                Log.d(TAG, "Pagar cuota del prestamo con id ${prestamo.id}")
                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("ID del préstamo seleccionado")
                builder.setMessage(prestamo.id)
                builder.setPositiveButton("OK") { _, _ -> }
                val dialog = builder.create()
                dialog.show()
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
