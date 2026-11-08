package com.vitalcare.serenia.core.designsystems.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Estado global del modo de alto contraste.
 *
 * Es un estado de Compose: cualquier composable que lea un color de [SereniaPalette]
 * se vuelve a componer solo cuando cambia. Lo escribe únicamente [SereniaTheme].
 */
object SereniaContrast {
    var enabled by mutableStateOf(false)
        internal set

    /** Color de borde: negro en alto contraste, el color original en modo normal. */
    fun border(normal: Color): Color = if (enabled) Color.Black else normal

    /** Grosor de borde: 1 dp más grueso en alto contraste. */
    fun stroke(normal: Dp): Dp = if (enabled) normal + 1.dp else normal
}

object SereniaPalette {
    val Teal50 = Color(0xFFEDF6F4)
    val Teal100 = Color(0xFFD3E9E5)
    val Teal200 = Color(0xFFA8D2CB)
    val Teal300 = Color(0xFF7AB8AE)
    val Teal400 = Color(0xFF4B9C90)
    val Teal500 = Color(0xFF2B8579)
    val Teal600 = Color(0xFF237A70)
    val Teal700 = Color(0xFF1F6F66)
    val Teal800 = Color(0xFF17554E)
    val Teal900 = Color(0xFF0F3B36)

    val Verde50 = Color(0xFFF0F8F3)
    val Verde100 = Color(0xFFDDF0E4)
    val Verde200 = Color(0xFFBFE2CC)
    val Verde300 = Color(0xFFA9D8B8)
    val Verde400 = Color(0xFF8CC9A1)
    val Verde500 = Color(0xFF6FBA8C)
    val Verde600 = Color(0xFF4FA070)
    val Verde700 = Color(0xFF3C8159)
    val Verde800 = Color(0xFF2F6446)
    val Verde900 = Color(0xFF1F4430)

    val Durazno50 = Color(0xFFFDF9F3)
    val Durazno100 = Color(0xFFFAF0E1)
    val Durazno200 = Color(0xFFF6E4CB)
    val Durazno300 = Color(0xFFF2D8B5)
    val Durazno400 = Color(0xFFE9C08D)
    val Durazno500 = Color(0xFFDBA566)
    val Durazno600 = Color(0xFFBC8346)
    val Durazno700 = Color(0xFF94642F)
    val Durazno800 = Color(0xFF6D4A24)
    val Durazno900 = Color(0xFF47301A)

    val Neutro50 = Color(0xFFFBFAF6)
    val Neutro100 = Color(0xFFF5F1EA)
    val Neutro200 = Color(0xFFE8E3D9)
    val Neutro300 = Color(0xFFD3CDC0)
    val Neutro400 = Color(0xFFA9A294)
    val Neutro500 = Color(0xFF7C766A)
    val Neutro600 = Color(0xFF5E5950)
    val Neutro700 = Color(0xFF433F38)
    val Neutro800 = Color(0xFF2C2925)
    val Neutro900 = Color(0xFF1C1A17)

    val Success = Color(0xFF2F7D4F)
    val SuccessBg = Color(0xFFE3F3E8)
    val Warning = Color(0xFF8A5A0B)
    val WarningText = Color(0xFF7A4F08)
    val WarningBg = Color(0xFFFCF0D6)
    val Error = Color(0xFFB94A36)
    val ErrorBg = Color(0xFFFBE7E2)
    val Info = Color(0xFF2F6F9A)
    val InfoBg = Color(0xFFE3EFF7)
}
