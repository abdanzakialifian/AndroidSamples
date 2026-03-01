package com.kotlin.androidsamples.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = BrandIndigo,
    onPrimary = Color.White,
    background = AppBg,
    onBackground = Slate900,
    surface = AppSurface,
    onSurface = Slate900,
    secondary = Slate500,
    onSecondary = Color.White,
    tertiary = SuccessGreen,
    onTertiary = Color.White,
    outline = Slate400,
    outlineVariant = AppOutline,
    surfaceVariant = AppBg,
    onSurfaceVariant = Slate500,
    error = ErrorRed,            // Mapping Error ke sini
    onError = Color.White,
)

@Composable
fun PlaygroundTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}