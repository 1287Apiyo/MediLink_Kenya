package com.example.medilinkapp.model

data class HealthData(
    val stepGoal: Int? = null,
    val waterIntake: Int = 0,
    val steps: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
