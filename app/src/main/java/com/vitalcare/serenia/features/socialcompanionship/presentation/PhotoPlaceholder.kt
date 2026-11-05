package com.vitalcare.serenia.features.socialcompanionship.presentation.family.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme

// Striped box that stands in for a family photo until real images are available
@Composable
fun PhotoPlaceholder(
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)
    val stripeColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)

    Box(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val spacing = 26.dp.toPx()
            val strokeWidth = 12.dp.toPx()
            var startX = -size.height

            while (startX < size.width) {
                drawLine(
                    color = stripeColor,
                    start = Offset(startX, size.height),
                    end = Offset(startX + size.height, 0f),
                    strokeWidth = strokeWidth
                )
                startX += spacing
            }
        }

        Text(
            text = "foto familiar",
            style = MaterialTheme.typography.labelLarge,
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9)
@Composable
fun PhotoPlaceholderPreview() {
    SereniaTheme {
        PhotoPlaceholder(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(130.dp)
        )
    }
}
