package com.vitalcare.serenia.core.designsystems.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

/** Cuánto crece el texto cuando el alto contraste está activo. */
private const val HIGH_CONTRAST_FONT_SCALE = 1.3f

private fun sereniaColorScheme(): ColorScheme = lightColorScheme(
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
    highContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    // Se escribe antes de componer el contenido para que todos los colores lean el valor nuevo.
    remember(highContrast) {
        SereniaContrast.enabled = highContrast
        highContrast
    }

    val density = LocalDensity.current
    val themedDensity = remember(density, highContrast) {
        if (highContrast) {
            Density(density.density, density.fontScale * HIGH_CONTRAST_FONT_SCALE)
        } else {
            density
        }
    }

    CompositionLocalProvider(LocalDensity provides themedDensity) {
        MaterialTheme(
            colorScheme = sereniaColorScheme(),
            typography = SereniaTypography,
            content = content
        )
    }
}
