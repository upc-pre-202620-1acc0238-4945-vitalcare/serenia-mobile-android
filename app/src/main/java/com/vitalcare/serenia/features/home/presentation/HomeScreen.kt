package com.vitalcare.serenia.features.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.features.alertsandsafety.presentation.HelpLayout
import com.vitalcare.serenia.features.alertsandsafety.presentation.HelpOutcome
import com.vitalcare.serenia.features.dailycheckin.presentation.CheckInSection
import com.vitalcare.serenia.features.dailycheckin.presentation.CheckInViewModel
import com.vitalcare.serenia.features.socialcompanionship.presentation.HomeReminderSection
import com.vitalcare.serenia.features.socialcompanionship.presentation.HomeReminderSnackbar

@Composable
fun HomeScreen(
    onHelpSent: (HelpOutcome) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val checkInViewModel: CheckInViewModel = hiltViewModel()
    val checkInState by checkInViewModel.state.collectAsStateWithLifecycle()
    val showsReminder = !state.simplifiedMode && checkInState.isQuestionSettled

    LifecycleResumeEffect(Unit) {
        viewModel.refreshGreeting()
        viewModel.loadUser()
        onPauseOrDispose { }
    }

    HelpLayout(onHelpSent = onHelpSent, modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            HomeContent(
                state = state,
                checkIn = {
                    CheckInSection(
                        firstName = state.firstName,
                        simplified = state.simplifiedMode,
                        viewModel = checkInViewModel
                    )
                },
                companionship = {
                    if (showsReminder) HomeReminderSection()
                }
            )

            HomeReminderSnackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(SereniaSpacing.md)
            )
        }
    }
}

@Composable
fun HomeContent(
    state: HomeUiState,
    modifier: Modifier = Modifier,
    checkIn: @Composable () -> Unit,
    companionship: @Composable () -> Unit = {}
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
            .verticalScroll(rememberScrollState())
            .padding(start = SereniaSpacing.screen, top = 28.dp, end = SereniaSpacing.screen, bottom = 8.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = state.date,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = SereniaPalette.Neutro600
            )
            Text(
                text = state.title,
                style = MaterialTheme.typography.headlineLarge,
                letterSpacing = (-0.005).em,
                color = SereniaPalette.Teal700,
                modifier = Modifier.semantics { heading() }
            )
        }

        checkIn()
        companionship()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun HomeContentPreview() {
    SereniaTheme {
        HomeContent(
            state = HomeUiState(date = "Jueves 5 de noviembre", greeting = "Buenos días", firstName = "Rosa"),
            checkIn = {}
        )
    }
}
