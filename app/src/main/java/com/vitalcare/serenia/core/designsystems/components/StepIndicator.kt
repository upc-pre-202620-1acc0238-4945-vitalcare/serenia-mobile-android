package com.vitalcare.serenia.core.designsystems.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun StepIndicator(
    current: Int,
    modifier: Modifier = Modifier,
    total: Int = 3
) {
    val label = "Paso $current de $total"

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.clearAndSetSemantics { contentDescription = label }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(total) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .background(
                            color = if (index < current) SereniaPalette.Teal700 else SereniaPalette.Neutro300,
                            shape = CircleShape
                        )
                )
            }
        }
        Text(
            text = label,
            fontFamily = Nunito,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Bold,
            color = SereniaPalette.Neutro700
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StepIndicatorPreview() {
    SereniaTheme {
        StepIndicator(current = 1)
    }
}
