package com.android.playground.watch.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme

private val LightColorScheme = ColorScheme(
    primary = BrandIndigo,
    onPrimary = Color.White,
    onBackground = Slate900,
    surfaceContainer = AppSurface,
    onSurface = Slate900,
    secondary = Slate500,
    onSecondary = Color.White,
    tertiary = SuccessGreen,
    onTertiary = Color.White,
    outline = Slate400,
    outlineVariant = AppOutline,
    onSurfaceVariant = Slate500,
    error = ErrorRed,            // Mapping Error ke sini
    onError = Color.White,
)

@Composable
fun WatchTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography,
        content = content
    )
}