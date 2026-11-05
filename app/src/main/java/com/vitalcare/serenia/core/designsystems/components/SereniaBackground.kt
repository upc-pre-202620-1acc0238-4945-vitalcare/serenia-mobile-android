package com.vitalcare.serenia.core.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystem.theme.dotCoral
import com.vitalcare.serenia.core.designsystem.theme.sunriseGlow

@Composable
fun SereniaBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val decorationColor = MaterialTheme.colorScheme.outlineVariant
    val leafColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Morning glow on the top right corner
            drawCircle(color = sunriseGlow, radius = size.width * 0.3f, center = Offset(size.width * 0.95f, 0f))

            // Hills on the bottom left corner
            drawCircle(
                color = decorationColor.copy(alpha = 0.35f),
                radius = size.width * 0.5f,
                center = Offset(size.width * 0.05f, size.height * 1.02f)
            )
            drawCircle(
                color = leafColor,
                radius = size.width * 0.32f,
                center = Offset(size.width * 0.05f, size.height * 1.02f)
            )

            drawLeaf(center = Offset(size.width * 0.06f, size.height * 0.4f), length = size.width * 0.2f, angle = -30f, color = Color.White)
            drawLeaf(center = Offset(size.width * 0.82f, size.height * 0.78f), length = size.width * 0.28f, angle = -50f, color = leafColor)
            drawLeaf(center = Offset(size.width * 0.9f, size.height * 0.87f), length = size.width * 0.22f, angle = -15f, color = leafColor)

            drawCircle(color = dotCoral, radius = 5.dp.toPx(), center = Offset(size.width * 0.92f, size.height * 0.68f))
            drawCircle(color = dotCoral.copy(alpha = 0.6f), radius = 3.dp.toPx(), center = Offset(size.width * 0.86f, size.height * 0.45f))
            drawCircle(color = Color.White, radius = 6.dp.toPx(), center = Offset(size.width * 0.14f, size.height * 0.6f))
            drawCircle(color = leafColor, radius = 4.dp.toPx(), center = Offset(size.width * 0.2f, size.height * 0.46f))
        }

        content()
    }
}

private fun DrawScope.drawLeaf(center: Offset, length: Float, angle: Float, color: Color) {
    val half = length / 2
    val leaf = Path().apply {
        moveTo(center.x - half, center.y)
        quadraticTo(center.x, center.y - half * 0.8f, center.x + half, center.y)
        quadraticTo(center.x, center.y + half * 0.8f, center.x - half, center.y)
        close()
    }
    rotate(degrees = angle, pivot = center) {
        drawPath(path = leaf, color = color)
    }
}
