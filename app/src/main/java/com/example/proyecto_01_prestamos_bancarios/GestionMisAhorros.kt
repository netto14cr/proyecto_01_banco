package com.example.proyecto_01_prestamos_bancarios

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.ktx.Firebase

class GestionMisAhorros : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestion_mis_ahorros)

        // empezar esta funcionalidad y terminarla mañana

        val db = Firebase.firestore
        val docRef = db.collection("mycollection").document("abc123")

        // Update the document with a new field
        val addOnFailureListener = docRef.update("newField", "newValue")
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
            }
            .addOnFailureListener {Log.w(TAG, "Error updating document")
            }


    }
}