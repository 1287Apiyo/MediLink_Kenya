package com.example.medilinkapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medilinkapp.model.Doctor
import com.example.medilinkapp.repository.FirestoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.util.*


class ConsultationViewModel : ViewModel() {
    private val repo = FirestoreRepository()
    private val firestore = FirebaseFirestore.getInstance()

    var category by mutableStateOf("")
    var method by mutableStateOf("")
    var email by mutableStateOf("")
    var dateTime by mutableStateOf("")

    var successMessage by mutableStateOf("")
    var errorMessage by mutableStateOf("")
    var selectedDoctor: Doctor? = null
    var isSubmitting by mutableStateOf(false)

    fun resetFields() {
        category = ""
        method = ""
        email = ""
        dateTime = ""
        errorMessage = ""
        successMessage = ""
        selectedDoctor = null
    }

    fun submitRequest() {
        successMessage = ""
        errorMessage = ""

        if (category.isBlank() || method.isBlank() || email.isBlank() || dateTime.isBlank()) {
            errorMessage = "All fields are required"
            return
        }

        viewModelScope.launch {
            try {
                isSubmitting = true

                val doctors = repo.getDoctors().filter {
                    it.specialization.equals(category, ignoreCase = true)
                }

                selectedDoctor = doctors.randomOrNull()

                if (selectedDoctor == null) {
                    errorMessage = "No doctor available for this category."
                    return@launch
                }

                val consultationData = hashMapOf(
                    "userEmail" to email,
                    "category" to category,
                    "method" to method,
                    "dateTime" to dateTime,
                    "doctorName" to selectedDoctor!!.name,
                    "doctorSpecialization" to selectedDoctor!!.specialization,
                    "timestamp" to Date()
                )

                firestore.collection("consultation_requests")
                    .add(consultationData)
                    .addOnSuccessListener {
                        successMessage = "Consultation with Dr. ${selectedDoctor!!.name} confirmed!"
                    }
                    .addOnFailureListener {
                        errorMessage = "Failed to save request. Try again."
                    }

            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "An unexpected error occurred"
            } finally {
                isSubmitting = false
            }
        }
    }
}