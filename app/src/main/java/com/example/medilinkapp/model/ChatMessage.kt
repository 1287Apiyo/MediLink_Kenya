package com.example.medilinkapp.model

data class ChatMessage(
    val sender: String = "",
    val message: String = "",
    val timestamp: Long = 0L
)
