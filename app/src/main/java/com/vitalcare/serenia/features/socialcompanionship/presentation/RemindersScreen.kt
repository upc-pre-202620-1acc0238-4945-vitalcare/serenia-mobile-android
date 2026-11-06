package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.add
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.SecondaryButton
import com.vitalcare.serenia.core.designsystems.components.SereniaSnackbar
import com.vitalcare.serenia.core.designsystems.eventAvailable
import com.vitalcare.serenia.core.designsystems.refresh
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderDateTime
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminder
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminderStatus
import kotlinx.coroutines.delay

@Composable
fun RemindersScreen(
    message: String?,
    onMessageShown: () -> Unit,
    onNewReminder: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RemindersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RefreshWhileResumed(onRefresh = viewModel::refresh)

    LaunchedEffect(message) {
        if (message != null) {
            viewModel.showMessage(message)
            onMessageShown()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        RemindersContent(
            state = state,
            onNewReminder = onNewReminder,
            onRetry = viewModel::refresh,
            onDone = viewModel::complete,
            onLater = viewModel::postpone,
            onCancel = viewModel::askToCancel,
            onConfirmCancel = viewModel::confirmCancel,
            onKeep = viewModel::keepReminder
        )

        ReminderSnackbar(
            state = state,
            onUndo = viewModel::undoComplete,
            onMessageShown = viewModel::onMessageShown,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(SereniaSpacing.md)
        )
    }
}

@Composable
fun ReminderSnackbar(
    state: RemindersUiState,
    onUndo: () -> Unit,
    onMessageShown: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(state.message) {
        if (state.message != null) {
            delay(MESSAGE_DURATION_MILLIS)
            onMessageShown()
        }
    }

    val message = state.message
    if (state.completedReminder != null) {
        SereniaSnackbar(
            message = "Listo, está hecho.",
            actionLabel = "Deshacer",
            onAction = onUndo,
            modifier = modifier
        )
    } else if (message != null) {
        SereniaSnackbar(message = message, modifier = modifier)
    }
}

@Composable
fun RemindersContent(
    state: RemindersUiState,
    onNewReminder: () -> Unit,
    onRetry: () -> Unit,
    onDone: (String) -> Unit,
    onLater: (String) -> Unit,
    onCancel: (String) -> Unit,
    onConfirmCancel: () -> Unit,
    onKeep: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
    ) {
        val screenHeight = maxHeight

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .heightIn(min = screenHeight)
                .padding(start = SereniaSpacing.screen, top = 28.dp, end = SereniaSpacing.screen, bottom = 96.dp)
        ) {
            Text(
                text = "Mis recordatorios",
                style = MaterialTheme.typography.headlineLarge,
                color = SereniaPalette.Teal700,
                modifier = Modifier.semantics { heading() }
            )

            val reminders = state.reminders
            val loadError = state.loadError
            when {
                loadError != null && reminders == null -> {
                    AlertBanner(message = loadError)
                    SecondaryButton(text = "Intentar de nuevo", icon = refresh, onClick = onRetry)
                }

                reminders == null -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 160.dp)
                ) {
                    CircularProgressIndicator(color = SereniaPalette.Teal700)
                }

                reminders.isEmpty() && state.completedReminder == null -> EmptyReminders(
                    onNewReminder = onNewReminder,
                    modifier = Modifier.heightIn(min = (screenHeight - EMPTY_STATE_OFFSET).coerceAtLeast(0.dp))
                )

                else -> {
                    SecondaryButton(
                        text = "Nuevo recordatorio",
                        icon = add,
                        onClick = onNewReminder,
                        containerColor = SereniaPalette.Teal50
                    )

                    state.dueReminders.forEach { reminder ->
                        ReminderItem(
                            reminder = reminder,
                            state = state,
                            onDone = onDone,
                            onLater = onLater,
                            onCancel = onCancel,
                            onConfirmCancel = onConfirmCancel,
                            onKeep = onKeep
                        )
                    }

                    ReminderSection(
                        title = "Hoy",
                        reminders = state.todayReminders,
                        state = state,
                        onCancel = onCancel,
                        onConfirmCancel = onConfirmCancel,
                        onKeep = onKeep
                    )

                    ReminderSection(
                        title = "Próximos",
                        reminders = state.upcomingReminders,
                        state = state,
                        onCancel = onCancel,
                        onConfirmCancel = onConfirmCancel,
                        onKeep = onKeep
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderSection(
    title: String,
    reminders: List<SocialReminder>,
    state: RemindersUiState,
    onCancel: (String) -> Unit,
    onConfirmCancel: () -> Unit,
    onKeep: () -> Unit
) {
    if (reminders.isEmpty()) return

    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = SereniaPalette.Neutro600,
        modifier = Modifier
            .padding(top = 4.dp)
            .semantics { heading() }
    )
    reminders.forEach { reminder ->
        ReminderItem(
            reminder = reminder,
            state = state,
            onDone = {},
            onLater = {},
            onCancel = onCancel,
            onConfirmCancel = onConfirmCancel,
            onKeep = onKeep
        )
    }
}

@Composable
private fun ReminderItem(
    reminder: SocialReminder,
    state: RemindersUiState,
    onDone: (String) -> Unit,
    onLater: (String) -> Unit,
    onCancel: (String) -> Unit,
    onConfirmCancel: () -> Unit,
    onKeep: () -> Unit
) {
    val errorMessage = state.actionError.takeIf { state.actionErrorReminderId == reminder.id }
    val isBusy = state.busyReminderId == reminder.id

    when {
        state.reminderToCancel == reminder.id -> CancelReminderCard(
            reminder = reminder,
            isCanceling = isBusy,
            errorMessage = errorMessage,
            onConfirm = onConfirmCancel,
            onKeep = onKeep
        )

        reminder.isDue -> DueReminderCard(
            reminder = reminder,
            isBusy = isBusy,
            errorMessage = errorMessage,
            onDone = { onDone(reminder.id) },
            onLater = { onLater(reminder.id) },
            onCancel = { onCancel(reminder.id) }
        )

        else -> ReminderCard(
            reminder = reminder,
            errorMessage = errorMessage,
            onCancel = { onCancel(reminder.id) }
        )
    }
}

@Composable
private fun EmptyReminders(
    onNewReminder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(104.dp)
                .background(SereniaPalette.Durazno200, CircleShape)
        ) {
            Icon(
                imageVector = eventAvailable,
                contentDescription = null,
                tint = SereniaPalette.Durazno900,
                modifier = Modifier.size(52.dp)
            )
        }
        Text(
            text = "Aún no tienes recordatorios",
            style = MaterialTheme.typography.headlineMedium,
            color = SereniaPalette.Neutro800,
            textAlign = TextAlign.Center,
            modifier = Modifier.semantics { heading() }
        )
        Text(
            text = "Para acordarte de llamar a una amiga o ir a una actividad.",
            style = MaterialTheme.typography.bodyMedium,
            color = SereniaPalette.Neutro700,
            textAlign = TextAlign.Center
        )
        PrimaryButton(
            text = "Nuevo recordatorio",
            icon = add,
            onClick = onNewReminder,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

private val EMPTY_STATE_OFFSET = 180.dp
private const val MESSAGE_DURATION_MILLIS = 4_000L

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun RemindersContentPreview() {
    val now = ReminderDateTime.now()
    SereniaTheme {
        RemindersContent(
            state = RemindersUiState(
                reminders = listOf(
                    SocialReminder("1", "Llamar a doña Carmen", now, SocialReminderStatus.PRESENTED),
                    SocialReminder("2", "Taller de tejido", now, SocialReminderStatus.SCHEDULED)
                )
            ),
            onNewReminder = {},
            onRetry = {},
            onDone = {},
            onLater = {},
            onCancel = {},
            onConfirmCancel = {},
            onKeep = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun EmptyRemindersContentPreview() {
    SereniaTheme {
        RemindersContent(
            state = RemindersUiState(reminders = emptyList()),
            onNewReminder = {},
            onRetry = {},
            onDone = {},
            onLater = {},
            onCancel = {},
            onConfirmCancel = {},
            onKeep = {}
        )
    }
}
