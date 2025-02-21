package com.example.medilinkapp.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    // State to manage email and password input
    val email = mutableStateOf("")
    val password = mutableStateOf("")

    // Function to handle login action
    fun login() {
        // Here, we would handle the login logic, such as validating the email and password.
        // For now, it's just a placeholder.
        if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
            // Perform login logic (e.g., API call, authentication, etc.)
            println("Logging in with email: ${email.value} and password: ${password.value}")
        } else {
            println("Please enter both email and password.")
        }
    }
}
