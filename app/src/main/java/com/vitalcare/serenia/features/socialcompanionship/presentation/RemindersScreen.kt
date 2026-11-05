package com.vitalcare.serenia.features.socialcompanionship.presentation.reminders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import com.vitalcare.serenia.core.designsystem.components.SereniaBackground
import com.vitalcare.serenia.core.designsystem.components.SereniaPrimaryButton
import com.vitalcare.serenia.core.designsystem.components.SereniaSnackbar
import com.vitalcare.serenia.core.designsystem.icon.add
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.features.socialcompanionship.domain.Reminder
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderStatus
import com.vitalcare.serenia.features.socialcompanionship.presentation.reminders.components.ReminderCard

@Composable
fun RemindersScreen(
    modifier: Modifier = Modifier,
    viewModel: RemindersViewModel = hiltViewModel(),
    onNewReminderClick: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val listState = rememberLazyListState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            val message = when (event) {
                RemindersEvent.ReminderCancelled -> "Recordatorio cancelado"
                RemindersEvent.ReminderSaved -> "Recordatorio guardado"
            }
            // The message runs on its own so it never holds back the next event
            launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(message)
            }
            if (event == RemindersEvent.ReminderSaved) {
                // The new reminder is the last card, right above the button that closes the list
                listState.animateScrollToItem(viewModel.uiState.value.reminders.size + 1)
            }
        }
    }

    RemindersContent(
        uiState = uiState,
        listState = listState,
        snackbarHostState = snackbarHostState,
        onDoneClick = viewModel::markAsDone,
        onPostponeClick = viewModel::postpone,
        onCancelClick = viewModel::cancel,
        onNewReminderClick = onNewReminderClick,
        modifier = modifier
    )
}

@Composable
fun RemindersContent(
    uiState: RemindersUiState,
    listState: LazyListState,
    snackbarHostState: SnackbarHostState,
    onDoneClick: (Int) -> Unit,
    onPostponeClick: (Int) -> Unit,
    onCancelClick: (Int) -> Unit,
    onNewReminderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SereniaBackground(modifier = modifier) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Text(
                    text = "Mis recordatorios",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Para acordarte de llamar a una amiga o ir a una actividad.",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    lineHeight = 28.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            items(items = uiState.reminders, key = { reminder -> reminder.id }) { reminder ->
                ReminderCard(
                    reminder = reminder,
                    onDoneClick = { onDoneClick(reminder.id) },
                    onPostponeClick = { onPostponeClick(reminder.id) },
                    onCancelClick = { onCancelClick(reminder.id) }
                )
            }

            item {
                SereniaPrimaryButton(
                    text = "Nuevo recordatorio",
                    onClick = onNewReminderClick,
                    icon = add,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) { snackbarData ->
            SereniaSnackbar(message = snackbarData.visuals.message)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RemindersScreenPreview() {
    SereniaTheme {
        RemindersContent(
            uiState = RemindersUiState(
                reminders = listOf(
                    Reminder(1, "Llamar a doña Carmen", "Hoy, 4:00 PM", ReminderStatus.PENDING, true),
                    Reminder(2, "Taller de tejido", "Mañana, 10:00 AM", ReminderStatus.PENDING, false),
                    Reminder(3, "Llamar a Rosario", "Ayer, 5:00 PM", ReminderStatus.MISSED, false)
                )
            ),
            listState = rememberLazyListState(),
            snackbarHostState = remember { SnackbarHostState() },
            onDoneClick = {},
            onPostponeClick = {},
            onCancelClick = {},
            onNewReminderClick = {}
        )
    }
}
