package com.vitalcare.serenia.features.dailycheckin.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.ChosenValueCard
import com.vitalcare.serenia.core.designsystems.components.FieldError
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.StepIndicator
import com.vitalcare.serenia.core.designsystems.components.TimeInputDialog
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.format.formatTime

@Composable
fun CheckInTimeScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CheckInTimeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            viewModel.onContinued()
            onContinue()
        }
    }

    CheckInTimeContent(
        state = state,
        modifier = modifier,
        onChangeTime = viewModel::openPicker,
        onContinue = viewModel::saveTime
    )

    if (state.isPickerVisible) {
        TimeInputDialog(
            title = "¿A qué hora?",
            initialHour = state.reminderTime.hour,
            initialMinute = state.reminderTime.minute,
            onConfirm = viewModel::onTimeChosen,
            onDismiss = viewModel::closePicker
        )
    }
}

@Composable
fun CheckInTimeContent(
    state: CheckInTimeUiState,
    onChangeTime: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
    ) {
        val screenHeight = maxHeight

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .heightIn(min = screenHeight)
                .padding(start = SereniaSpacing.authScreen, top = 20.dp, end = SereniaSpacing.authScreen, bottom = 24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    StepIndicator(current = 2)
                    Text(
                        text = "¿A qué hora te preguntamos cómo amaneciste?",
                        style = MaterialTheme.typography.headlineLarge,
                        color = SereniaPalette.Teal700,
                        modifier = Modifier.semantics { heading() }
                    )
                    Text(
                        text = "Cada día te haremos una pregunta corta. Podrás cambiar la hora cuando quieras.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SereniaPalette.Neutro700
                    )
                }

                state.formError?.let { message ->
                    AlertBanner(message = message)
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    ChosenValueCard(
                        label = "Hora de tu pregunta",
                        value = formatTime(state.reminderTime.hour, state.reminderTime.minute),
                        onChange = onChangeTime,
                        isError = state.timeError != null,
                        errorMessage = state.timeError
                    )
                    val timeError = state.timeError
                    if (timeError != null) {
                        FieldError(message = timeError)
                    } else {
                        Text(
                            text = "Puedes elegir entre las 6:00 a. m. y las 8:00 p. m.",
                            fontFamily = Nunito,
                            fontSize = 18.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SereniaPalette.Neutro700
                        )
                    }
                }
            }

            PrimaryButton(
                text = "Continuar",
                onClick = onContinue,
                isLoading = state.isSaving,
                modifier = Modifier.padding(top = SereniaSpacing.lg)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun CheckInTimeContentPreview() {
    SereniaTheme {
        CheckInTimeContent(
            state = CheckInTimeUiState(),
            onChangeTime = {},
            onContinue = {}
        )
    }
}
