// Theme.kt
package com.example.medilinkapp.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF0D47A1), // Dark Blue
    onPrimary = Color.White,
    secondary = Color(0xFF1976D2), // Lighter Blue
    onSecondary = Color.White,
    background = Color(0xFFE3F2FD), // Lightest Blue
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
)

@Composable
fun MedilinkAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = Typography,
        content = content
    )
}
