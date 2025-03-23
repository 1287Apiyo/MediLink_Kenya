package com.example.medilinkapp.repository

import com.example.medilinkapp.model.Doctor
import com.example.medilinkapp.model.Prescription
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Fetch doctors from Firestore
    suspend fun getDoctors(): List<Doctor> {
        val snapshot = firestore.collection("doctors").get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Doctor::class.java)
        }
    }

    suspend fun getPrescriptions(): List<Prescription> {
        val snapshot = firestore.collection("Prescription").get().await()
        println("Fetched ${snapshot.documents.size} prescriptions from Firestore")
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Prescription::class.java)?.copy(id = doc.id)
        }
    }

}