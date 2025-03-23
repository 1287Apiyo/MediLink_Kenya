package com.example.medilinkapp.model

data class Prescription(
    val id: String = "",
    val doctorName: String = "",
    val date: String = "",
    val medications: List<String> = emptyList()
)
