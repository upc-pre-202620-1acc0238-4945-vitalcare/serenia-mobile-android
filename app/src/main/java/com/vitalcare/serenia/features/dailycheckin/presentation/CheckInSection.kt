package com.vitalcare.serenia.features.dailycheckin.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun CheckInSection(
    firstName: String?,
    simplified: Boolean,
    modifier: Modifier = Modifier,
    viewModel: CheckInViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CheckInSectionContent(
        state = state,
        firstName = firstName,
        simplified = simplified,
        modifier = modifier,
        onAnswer = viewModel::answer,
        onSkip = viewModel::skipToday,
        onResume = viewModel::resumeToday,
        onRetry = viewModel::loadCheckIn
    )
}

@Composable
fun CheckInSectionContent(
    state: CheckInUiState,
    firstName: String?,
    simplified: Boolean,
    onAnswer: (MoodAnswer) -> Unit,
    onSkip: () -> Unit,
    onResume: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        state.actionError?.let { message ->
            AlertBanner(message = message)
        }

        when (val content = state.content) {
            CheckInContent.Loading -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp)
            ) {
                CircularProgressIndicator(color = SereniaPalette.Teal700)
            }

            is CheckInContent.Pending -> QuestionCard(
                question = content.question,
                simplified = simplified,
                enabled = !state.isSubmitting,
                onAnswer = onAnswer,
                onSkip = onSkip
            )

            is CheckInContent.Answered -> AnsweredCard(
                firstName = firstName,
                answer = content.answer,
                simplified = simplified
            )

            CheckInContent.Paused -> PauseCard(
                simplified = simplified,
                isLoading = state.isSubmitting,
                onResume = onResume
            )

            CheckInContent.Missed -> CheckInMessageCard(
                title = "Hoy ya pasó la hora de tu pregunta",
                body = "Mañana te volvemos a preguntar cómo amaneciste."
            )

            CheckInContent.Unavailable -> CheckInMessageCard(
                title = "Tu pregunta de hoy aún no está lista",
                body = "Vuelve en un rato para contarnos cómo estás.",
                actionLabel = "Volver a revisar",
                onAction = onRetry
            )

            is CheckInContent.Failed -> CheckInMessageCard(
                title = "No pudimos cargar tu pregunta",
                body = content.message,
                actionLabel = "Intentar de nuevo",
                onAction = onRetry
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun CheckInSectionContentPreview() {
    SereniaTheme {
        CheckInSectionContent(
            state = CheckInUiState(content = CheckInContent.Pending("1", "¿Cómo amaneciste hoy?")),
            firstName = "Rosa",
            simplified = false,
            onAnswer = {},
            onSkip = {},
            onResume = {},
            onRetry = {},
            modifier = Modifier.padding(20.dp)
        )
    }
}
