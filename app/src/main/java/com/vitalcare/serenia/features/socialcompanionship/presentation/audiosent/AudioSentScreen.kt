package com.vitalcare.serenia.features.socialcompanionship.presentation.audiosent

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
import com.vitalcare.serenia.core.designsystem.components.SereniaSecondaryButton
import com.vitalcare.serenia.core.designsystem.icon.check
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme

@Composable
fun AudioSentScreen(
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConfirmationLayout(
        title = "Audio enviado.",
        message = "Tu familia ya puede escucharlo.",
        titleColor = MaterialTheme.colorScheme.onBackground,
        illustration = {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = check,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(56.dp)
                )
            }
        },
        modifier = modifier
    ) {
        SereniaSecondaryButton(text = "Volver al inicio", onClick = onBackToHome)
    }
}

@Preview(showBackground = true)
@Composable
fun AudioSentScreenPreview() {
    SereniaTheme {
        AudioSentScreen(onBackToHome = {})
    }
}
