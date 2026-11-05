package com.vitalcare.serenia.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val baseline = Typography()

val AppTypography = Typography(
    displaySmall = baseline.displaySmall.copy(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp
    ),
    titleLarge = baseline.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
    titleMedium = baseline.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
    labelLarge = baseline.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp)
)
