package com.vitalcare.serenia.features.checkin.presentation.thanks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystem.components.ConfirmationLayout
import com.vitalcare.serenia.core.designsystem.components.SereniaLogo
import com.vitalcare.serenia.core.designsystem.components.SereniaSecondaryButton
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.features.checkin.domain.Mood

@Composable
fun CheckInThanksScreen(
    mood: Mood,
    modifier: Modifier = Modifier,
    viewModel: CheckInThanksViewModel = hiltViewModel(),
    onBackToHome: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(mood) {
        viewModel.loadThanks(mood)
    }

    CheckInThanksContent(
        uiState = uiState,
        onBackToHome = onBackToHome,
        modifier = modifier
    )
}

@Composable
fun CheckInThanksContent(
    uiState: CheckInThanksUiState,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConfirmationLayout(
        title = uiState.title,
        message = uiState.message,
        illustration = { SereniaLogo(size = 110.dp) },
        modifier = modifier
    ) {
        SereniaSecondaryButton(text = "Volver al inicio", onClick = onBackToHome)
    }
}

@Preview(showBackground = true)
@Composable
fun CheckInThanksScreenPreview() {
    SereniaTheme {
        CheckInThanksContent(
            uiState = CheckInThanksUiState(
                title = "Gracias, Rosa. Tu familia sabrá que estás bien.",
                message = "Tu familia ya puede ver cómo amaneciste."
            ),
            onBackToHome = {}
        )
    }
}
