package com.example.medilinkapp.model

import java.util.Date

data class ConsultationRequest(
    val userEmail: String,
    val category: String,
    val method: String,
    val dateTime: String,
    val doctorName: String,
    val doctorSpecialization: String,
    val timestamp: Date
)

