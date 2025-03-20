package com.example.medilinkapp.ui.screens.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.medilinkapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit = {}) {
    // Launch a coroutine that waits for 2 seconds then calls onTimeout
    LaunchedEffect(key1 = true) {
        delay(2000L)
        onTimeout()
    }

    // Full screen box with your primary color as background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        // Display the app logo (ensure R.drawable.logo exists in your resources)
        Image(
            painter = painterResource(id = R.drawable.medi),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )
    }
}
