package com.example.medilinkapp.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.example.medilinkapp.R
import com.example.medilinkapp.model.Doctor

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun getDoctors(): List<Doctor> {
        return try {
            val snapshot = db.collection("doctors").get().await()
            snapshot.documents.mapIndexed { index, doc ->
                val doctor = doc.toObject(Doctor::class.java)
                doctor?.copy(drawableId = getDoctorImage(index)) ?: Doctor()
            }
        } catch (e: Exception) {
            emptyList() // Return empty if error
        }
    }

    private fun getDoctorImage(index: Int): Int {
        val images = listOf(
            R.drawable.doctor1,
            R.drawable.doctor2,
            R.drawable.doctor3
        )
        return images[index % images.size] // Cycle through images
    }
}
