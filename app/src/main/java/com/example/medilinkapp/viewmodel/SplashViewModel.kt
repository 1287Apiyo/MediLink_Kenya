package com.example.medilinkapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    fun navigateToNextScreen(onNavigate: () -> Unit) {
        viewModelScope.launch {
            delay(3000) // 3-second splash delay
            onNavigate() // Navigate after delay
        }
    }
}
