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

private val DarkColors = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color.Black,
    secondary = Color(0xFF64B5F6),
    onSecondary = Color.Black,
    background = Color(0xFF121212),
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E),
    onSurface = Color.White,
)

@Composable
fun MedilinkAppTheme(
    darkTheme: Boolean = false, // <-- Add this parameter
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
