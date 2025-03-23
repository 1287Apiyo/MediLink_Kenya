package com.example.medilinkapp.model

data class HealthData(
    val userId: String = "",
    val steps: Int = 0,
    val heartRate: Int = 0,
    val sleepHours: Float = 0f
)
