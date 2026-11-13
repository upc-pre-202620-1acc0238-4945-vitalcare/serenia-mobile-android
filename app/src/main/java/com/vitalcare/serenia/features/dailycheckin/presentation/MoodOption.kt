package com.vitalcare.serenia.features.dailycheckin.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun MoodOption(
    answer: MoodAnswer,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    large: Boolean = false,
    enabled: Boolean = true
) {
    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(SereniaRadius.lg),
        color = answer.background,
        border = BorderStroke(if (large) 3.dp else 2.dp, answer.border),
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = if (large) 96.dp else 72.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(if (large) 20.dp else 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                start = if (large) 12.dp else 8.dp,
                top = if (large) 12.dp else 8.dp,
                end = if (large) 24.dp else 20.dp,
                bottom = if (large) 12.dp else 8.dp
            )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(if (large) 72.dp else 56.dp)
                    .background(answer.circle, CircleShape)
            ) {
                Icon(
                    imageVector = answer.icon,
                    contentDescription = null,
                    tint = answer.face,
                    modifier = Modifier.size(if (large) 48.dp else 36.dp)
                )
            }
            Text(
                text = answer.label,
                style = if (large) MaterialTheme.typography.headlineSmall else MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Neutro800
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun MoodOptionPreview() {
    SereniaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            MoodAnswer.entries.forEach { answer ->
                MoodOption(answer = answer, onClick = {})
            }
            MoodOption(answer = MoodAnswer.GOOD, onClick = {}, large = true)
        }
    }
}
