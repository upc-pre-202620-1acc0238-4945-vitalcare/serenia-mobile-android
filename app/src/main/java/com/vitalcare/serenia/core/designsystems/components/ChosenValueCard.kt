package com.vitalcare.serenia.core.designsystems.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

enum class ChosenValueTone {
    Teal,
    Verde
}

@Composable
fun ChosenValueCard(
    label: String,
    value: String,
    onChange: () -> Unit,
    modifier: Modifier = Modifier,
    tone: ChosenValueTone = ChosenValueTone.Teal,
    actionLabel: String = "Cambiar",
    isError: Boolean = false,
    errorMessage: String? = null,
    compact: Boolean = false
) {
    val shape = RoundedCornerShape(SereniaRadius.md)
    val background = if (tone == ChosenValueTone.Teal) SereniaPalette.Teal50 else SereniaPalette.Verde50
    val border = when {
        isError -> BorderStroke(3.dp, SereniaPalette.Error)
        tone == ChosenValueTone.Teal -> BorderStroke(2.dp, SereniaPalette.Teal200)
        else -> BorderStroke(2.dp, SereniaPalette.Verde300)
    }

    Surface(
        onClick = onChange,
        shape = shape,
        color = background,
        border = border,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = if (compact) 72.dp else 80.dp)
            .clearAndSetSemantics {
                contentDescription = "$label: $value. $actionLabel"
                if (isError && errorMessage != null) error(errorMessage)
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                start = if (compact) 18.dp else 20.dp,
                top = if (compact) 10.dp else 12.dp,
                end = 16.dp,
                bottom = if (compact) 10.dp else 12.dp
            )
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label,
                    fontFamily = Nunito,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SereniaPalette.Neutro700
                )
                Text(
                    text = value,
                    fontFamily = Nunito,
                    fontSize = if (compact) 22.sp else 28.sp,
                    lineHeight = if (compact) 28.sp else 34.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SereniaPalette.Neutro800
                )
            }
            Text(
                text = actionLabel,
                fontFamily = Nunito,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Teal700,
                modifier = Modifier
                    .background(SereniaPalette.Neutro50, CircleShape)
                    .border(BorderStroke(2.dp, SereniaPalette.Teal700), CircleShape)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun ChosenValueCardPreview() {
    SereniaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            ChosenValueCard(label = "Hora de tu pregunta", value = "10:00 a. m.", onChange = {})
            ChosenValueCard(label = "Hora de tu pregunta", value = "10:30 p. m.", onChange = {}, isError = true)
            ChosenValueCard(
                label = "Día elegido",
                value = "Sábado 7 de noviembre",
                onChange = {},
                tone = ChosenValueTone.Verde,
                compact = true
            )
        }
    }
}
