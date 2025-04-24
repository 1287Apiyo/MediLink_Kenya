package com.example.medilinkapp.repository

import com.example.medilinkapp.model.Doctor
import com.example.medilinkapp.model.Prescription
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // ✅ Clean and coroutine-friendly version
    suspend fun getDoctors(): List<Doctor> {
        return try {
            val snapshot = firestore.collection("doctors").get().await()
            snapshot.documents.mapNotNull { it.toObject(Doctor::class.java) }
        } catch (e: Exception) {
            emptyList() // or throw e if you want to handle it upstream
        }
    }

    // ✅ Same cleanup for prescriptions
    suspend fun getPrescriptions(): List<Prescription> {
        return try {
            val snapshot = firestore.collection("prescriptions").get().await()
            snapshot.documents.mapNotNull { it.toObject(Prescription::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
