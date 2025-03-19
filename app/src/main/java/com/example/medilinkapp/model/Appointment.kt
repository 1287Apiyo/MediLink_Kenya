package com.example.medilinkapp.model

data class Appointment(
    val doctorName: String = "",
    val appointmentDate: String = "", // e.g., "25/12/2025"
    val appointmentTime: String = "", // e.g., "14:30"
    val notes: String = "",
    val timestamp: Long = 0L,
    val status: String = "" // "upcoming" or "past"
)
