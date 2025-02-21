package com.example.medilinkapp.ui.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    fun navigateToNextScreen() {
        // Logic to navigate to the next screen after a delay (e.g., login or dashboard)
        viewModelScope.launch {
            delay(2000) // Wait for 2 seconds
            // Call navigation logic here (for example, navigate to LoginScreen)
        }
    }
}