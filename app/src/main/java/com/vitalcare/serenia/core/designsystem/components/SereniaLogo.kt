package com.vitalcare.serenia.core.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystem.theme.leafLight
import com.vitalcare.serenia.core.designsystem.theme.primaryLight
import com.vitalcare.serenia.core.designsystem.theme.secondaryContainerLight

@Composable
fun SereniaLogo(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp
) {
    Canvas(modifier = modifier.size(size)) {
        // The logo is drawn on a 48x48 grid and scaled to the requested size
        val unit = this.size.width / 48f

        val leftLeaf = Path().apply {
            moveTo(22f * unit, 44f * unit)
            cubicTo(8f * unit, 44f * unit, 2f * unit, 32f * unit, 4f * unit, 18f * unit)
            cubicTo(16f * unit, 20f * unit, 22f * unit, 30f * unit, 22f * unit, 44f * unit)
            close()
        }
        val rightLeaf = Path().apply {
            moveTo(26f * unit, 44f * unit)
            cubicTo(40f * unit, 44f * unit, 46f * unit, 32f * unit, 44f * unit, 18f * unit)
            cubicTo(32f * unit, 20f * unit, 26f * unit, 30f * unit, 26f * unit, 44f * unit)
            close()
        }

        drawPath(path = leftLeaf, color = leafLight)
        drawPath(path = rightLeaf, color = primaryLight)
        drawCircle(color = primaryLight.copy(alpha = 0.8f), radius = 6f * unit, center = Offset(18f * unit, 11f * unit))
        drawCircle(color = secondaryContainerLight, radius = 5.5f * unit, center = Offset(31f * unit, 14f * unit))
    }
}

@Preview(showBackground = true)
@Composable
fun SereniaLogoPreview() {
    SereniaLogo(size = 96.dp)
}
