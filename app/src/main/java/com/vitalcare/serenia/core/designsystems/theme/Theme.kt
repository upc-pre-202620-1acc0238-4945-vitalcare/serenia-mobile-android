package com.vitalcare.serenia.core.designsystems.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val lightScheme = lightColorScheme(
    primary = SereniaPalette.Teal700,
    onPrimary = SereniaPalette.Neutro50,
    primaryContainer = SereniaPalette.Teal50,
    onPrimaryContainer = SereniaPalette.Teal800,
    secondary = SereniaPalette.Verde800,
    onSecondary = SereniaPalette.Neutro50,
    secondaryContainer = SereniaPalette.Verde50,
    onSecondaryContainer = SereniaPalette.Verde900,
    tertiary = SereniaPalette.Durazno800,
    onTertiary = SereniaPalette.Neutro50,
    tertiaryContainer = SereniaPalette.Durazno100,
    onTertiaryContainer = SereniaPalette.Durazno900,
    error = SereniaPalette.Error,
    onError = SereniaPalette.Neutro50,
    errorContainer = SereniaPalette.ErrorBg,
    onErrorContainer = SereniaPalette.Neutro800,
    background = SereniaPalette.Neutro100,
    onBackground = SereniaPalette.Neutro800,
    surface = SereniaPalette.Neutro50,
    onSurface = SereniaPalette.Neutro800,
    surfaceVariant = SereniaPalette.Neutro200,
    onSurfaceVariant = SereniaPalette.Neutro700,
    outline = SereniaPalette.Neutro500,
    outlineVariant = SereniaPalette.Neutro200,
)

@Composable
fun SereniaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightScheme,
        typography = AppTypography,
        content = content
    )
}
