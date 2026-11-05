package com.vitalcare.serenia.features.checkin.presentation.skipped

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystem.components.ConfirmationLayout
import com.vitalcare.serenia.core.designsystem.components.SereniaPrimaryButton
import com.vitalcare.serenia.core.designsystem.components.SereniaSecondaryButton
import com.vitalcare.serenia.core.designsystem.icon.bedtime
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme

@Composable
fun CheckInSkippedScreen(
    onResumeCheckIn: () -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConfirmationLayout(
        title = "Hoy no te preguntamos nada.",
        message = "Aquí estamos cuando quieras. Tu familia sabe que hoy elegiste una pausa.",
        illustration = {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = bedtime,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(48.dp)
                )
            }
        },
        modifier = modifier
    ) {
        SereniaPrimaryButton(text = "Retomar mis preguntas", onClick = onResumeCheckIn)
        SereniaSecondaryButton(text = "Volver al inicio", onClick = onBackToHome)
    }
}

@Preview(showBackground = true)
@Composable
fun CheckInSkippedScreenPreview() {
    SereniaTheme {
        CheckInSkippedScreen(onResumeCheckIn = {}, onBackToHome = {})
    }
}
