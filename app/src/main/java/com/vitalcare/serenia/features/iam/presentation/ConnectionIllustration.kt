package com.vitalcare.serenia.features.iam.presentation.welcome.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystem.theme.forestDark
import com.vitalcare.serenia.core.designsystem.theme.sunriseDeep
import com.vitalcare.serenia.features.iam.presentation.components.HeartBadge

private val MintBubble = Color(0xFFBFE3D2)

/** Two people (M and A) linked through a heart: the idea behind Serenia in one picture. */
@Composable
fun ConnectionIllustration(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InitialBubble(initial = "M", color = sunriseDeep)
        DashedConnector(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .size(68.dp)
                .background(Color.White.copy(alpha = 0.12f), CircleShape)
                .border(2.dp, Color.White.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            HeartBadge(size = 44.dp, onCoral = true)
        }
        DashedConnector(modifier = Modifier.weight(1f))
        InitialBubble(initial = "A", color = MintBubble)
    }
}

@Composable
private fun InitialBubble(initial: String, color: Color) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .background(color, CircleShape)
            .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.titleLarge,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp,
            color = forestDark
        )
    }
}

@Composable
private fun DashedConnector(modifier: Modifier = Modifier) {
    Canvas(
        modifier = modifier
            .height(2.dp)
            .padding(horizontal = 4.dp)
    ) {
        drawLine(
            color = Color.White.copy(alpha = 0.55f),
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = 2.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10.dp.toPx(), 8.dp.toPx()))
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF173A33)
@Composable
fun ConnectionIllustrationPreview() {
    SereniaTheme {
        ConnectionIllustration(modifier = Modifier.padding(16.dp))
    }
}
