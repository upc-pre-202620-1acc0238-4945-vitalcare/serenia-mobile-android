package com.vitalcare.serenia.features.socialcompanionship.presentation.newreminder

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystem.components.SereniaBackground
import com.vitalcare.serenia.core.designsystem.components.SereniaPrimaryButton
import com.vitalcare.serenia.core.designsystem.icon.chevronLeft
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderSchedule
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderType
import com.vitalcare.serenia.features.socialcompanionship.presentation.newreminder.components.ReminderOption

@Composable
fun NewReminderScreen(
    modifier: Modifier = Modifier,
    viewModel: NewReminderViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onReminderSaved: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NewReminderEvent.ReminderSaved -> onReminderSaved()
            }
        }
    }

    NewReminderContent(
        uiState = uiState,
        onBackClick = onBack,
        onTypeClick = viewModel::selectType,
        onScheduleClick = viewModel::selectSchedule,
        onSaveClick = viewModel::saveReminder,
        modifier = modifier
    )
}

@Composable
fun NewReminderContent(
    uiState: NewReminderUiState,
    onBackClick: () -> Unit,
    onTypeClick: (ReminderType) -> Unit,
    onScheduleClick: (ReminderSchedule) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SereniaBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            TextButton(
                onClick = onBackClick,
                modifier = Modifier.heightIn(min = 56.dp),
                contentPadding = PaddingValues(start = 0.dp, end = 12.dp)
            ) {
                Icon(
                    imageVector = chevronLeft,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Atrás", style = MaterialTheme.typography.titleLarge)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "¿Qué quieres recordar?",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ReminderType.entries.forEach { type ->
                    ReminderOption(
                        text = type.label,
                        isSelected = uiState.selectedType == type,
                        onClick = { onTypeClick(type) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "¿Cuándo?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ReminderSchedule.entries.forEach { schedule ->
                    ReminderOption(
                        text = schedule.label,
                        isSelected = uiState.selectedSchedule == schedule,
                        onClick = { onScheduleClick(schedule) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            SereniaPrimaryButton(
                text = "Guardar recordatorio",
                onClick = onSaveClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewReminderScreenPreview() {
    SereniaTheme {
        NewReminderContent(
            uiState = NewReminderUiState(),
            onBackClick = {},
            onTypeClick = {},
            onScheduleClick = {},
            onSaveClick = {}
        )
    }
}
