package com.example.medilinkapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medilinkapp.model.Consultation
import com.example.medilinkapp.model.Doctor
import com.example.medilinkapp.repository.FirestoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class ConsultationViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val repo = FirestoreRepository()

    // Variables for consultation details
    var category by mutableStateOf("")
    var method by mutableStateOf("")
    var email by mutableStateOf("")
    var dateTime by mutableStateOf("")

    // Status messages
    var successMessage by mutableStateOf("")
    var errorMessage by mutableStateOf("")
    var isSubmitting by mutableStateOf(false)

    // Selected doctor and consultation list
    var selectedDoctor by mutableStateOf<Doctor?>(null)
    var consultations by mutableStateOf<List<Consultation>>(emptyList())

    // Fetch consultations from Firestore
    fun fetchConsultations() {
        viewModelScope.launch {
            try {
                val consultationsList = firestore.collection("consultation_requests")
                    .get()
                    .await()
                    .documents
                    .mapNotNull { document ->
                        document.toObject(Consultation::class.java)?.apply {
                            id = document.id
                        }
                    }
                consultations = consultationsList
            } catch (e: Exception) {
                errorMessage = "Failed to fetch consultations: ${e.localizedMessage}"
            }
        }
    }

    // 🔥 Delete a consultation from Firestore and update the local list
    fun deleteConsultation(consultation: Consultation) {
        viewModelScope.launch {
            try {
                consultation.id?.let { docId ->
                    firestore.collection("consultation_requests").document(docId).delete().await()

                    // Update the local list
                    consultations = consultations.filterNot { it.id == docId }

                    successMessage = "Consultation deleted successfully."
                } ?: run {
                    errorMessage = "Consultation ID is null."
                }
            } catch (e: Exception) {
                errorMessage = "Failed to delete consultation: ${e.localizedMessage}"
            }
        }
    }

    // Get a consultation by ID
    fun getConsultationById(consultationId: String): Consultation? {
        return consultations.find { it.id == consultationId }
    }

    // Reset fields after submission
    fun resetFields() {
        category = ""
        method = ""
        email = ""
        dateTime = ""
        errorMessage = ""
        successMessage = ""
        selectedDoctor = null
    }

    // Submit consultation request
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
                        fetchConsultations() // Refresh the list after adding
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
