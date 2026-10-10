package com.vitalcare.serenia.features.home.presentation.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystem.theme.forestDark
import com.vitalcare.serenia.core.designsystem.theme.onSecondaryContainerLight
import com.vitalcare.serenia.core.designsystem.theme.primaryContainerLight
import com.vitalcare.serenia.core.designsystem.theme.primaryLight
import com.vitalcare.serenia.core.designsystem.theme.secondaryContainerLight
import com.vitalcare.serenia.features.checkin.domain.Mood

private data class MoodColors(
    val outer: Color,
    val face: Color,
    val features: Color
)

private fun Mood.colors(): MoodColors = when (this) {
    Mood.GOOD -> MoodColors(outer = primaryContainerLight, face = forestDark, features = primaryContainerLight)
    Mood.NEUTRAL -> MoodColors(outer = secondaryContainerLight, face = onSecondaryContainerLight, features = secondaryContainerLight)
    Mood.NOT_GOOD -> MoodColors(outer = Color(0xFFBFDCD5), face = primaryLight, features = forestDark)
}

@Composable
fun MoodFace(
    mood: Mood,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 100.dp
) {
    val colors = mood.colors()

    Canvas(modifier = modifier.size(size)) {
        val radius = this.size.minDimension / 2
        val faceRadius = radius * 0.62f
        val stroke = radius * 0.08f

        if (isSelected) {
            drawCircle(color = Color.White, radius = radius)
            drawCircle(color = primaryLight, radius = radius - stroke / 2, style = Stroke(width = stroke))
        } else {
            drawCircle(color = colors.outer, radius = radius)
        }
        drawCircle(color = colors.face, radius = faceRadius)

        // Eyes
        val eyeRadius = faceRadius * 0.11f
        val eyeY = center.y - faceRadius * 0.22f
        drawCircle(color = colors.features, radius = eyeRadius, center = Offset(center.x - faceRadius * 0.35f, eyeY))
        drawCircle(color = colors.features, radius = eyeRadius, center = Offset(center.x + faceRadius * 0.35f, eyeY))

        // Mouth
        val mouthWidth = faceRadius * 0.9f
        val mouthStroke = Stroke(width = faceRadius * 0.1f, cap = StrokeCap.Round)
        when (mood) {
            Mood.GOOD -> drawArc(
                color = colors.features,
                startAngle = 20f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(center.x - mouthWidth / 2, center.y - mouthWidth * 0.35f),
                size = Size(mouthWidth, mouthWidth * 0.8f),
                style = mouthStroke
            )

            Mood.NEUTRAL -> drawLine(
                color = colors.features,
                start = Offset(center.x - mouthWidth * 0.35f, center.y + faceRadius * 0.3f),
                end = Offset(center.x + mouthWidth * 0.35f, center.y + faceRadius * 0.3f),
                strokeWidth = mouthStroke.width,
                cap = StrokeCap.Round
            )

            Mood.NOT_GOOD -> drawArc(
                color = colors.features,
                startAngle = 200f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(center.x - mouthWidth / 2, center.y + faceRadius * 0.25f),
                size = Size(mouthWidth, mouthWidth * 0.8f),
                style = mouthStroke
            )
        }
    }
}

@Composable
fun MoodOption(
    mood: Mood,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(110.dp)
            .clickable(role = Role.RadioButton, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MoodFace(mood = mood, isSelected = isSelected)
        Text(
            text = mood.label,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MoodOptionPreview() {
    SereniaTheme {
        Row {
            MoodOption(mood = Mood.GOOD, isSelected = false, onClick = {})
            MoodOption(mood = Mood.NEUTRAL, isSelected = false, onClick = {})
            MoodOption(mood = Mood.NOT_GOOD, isSelected = true, onClick = {})
        }
    }
}
