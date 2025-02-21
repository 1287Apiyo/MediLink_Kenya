package com.example.medilinkapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColorScheme(
    primary = SoftGreen,
    secondary = AccentGold,
    background = LightGrey,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = DarkGrey,
    onBackground = TextColor,
    onSurface = DarkGrey
)

private val DarkColorPalette = darkColorScheme(
    primary = SoftGreen,
    secondary = AccentGold,
    background = Color(0xFF212121),
    surface = Color(0xFF333333),
    onPrimary = Color.Black,
    onSecondary = DarkGrey,
    onBackground = Color.White,
    onSurface = Color.White
)

@Composable
fun MedilinkAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
