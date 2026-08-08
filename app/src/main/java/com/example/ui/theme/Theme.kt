package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SalonColorScheme = lightColorScheme(
    primary = SalonPinkPrimary,
    onPrimary = Color.White,
    primaryContainer = SalonPinkContainer,
    onPrimaryContainer = SalonDarkText,
    secondary = SalonPurplePrimary,
    onSecondary = Color.White,
    secondaryContainer = SalonPurpleContainer,
    onSecondaryContainer = SalonDarkText,
    tertiary = SalonGold,
    onTertiary = Color(0xFF3E2723),
    background = SalonBackground,
    onBackground = SalonDarkText,
    surface = SalonCardBackground,
    onSurface = SalonDarkText,
    surfaceVariant = SalonPinkContainer,
    onSurfaceVariant = SalonSubtext
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = SalonColorScheme,
        typography = Typography,
        content = content
    )
}
