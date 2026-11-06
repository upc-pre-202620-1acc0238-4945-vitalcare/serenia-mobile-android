package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.SecondaryButton
import com.vitalcare.serenia.core.designsystems.eventAvailable
import com.vitalcare.serenia.core.designsystems.schedule
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.format.formatRelativeDay
import com.vitalcare.serenia.core.format.formatTime
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderDateTime
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminder
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminderStatus

@Composable
fun HomeReminderSection(
    modifier: Modifier = Modifier,
    viewModel: RemindersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RefreshWhileResumed(onRefresh = viewModel::refresh)

    HomeReminderSectionContent(
        state = state,
        modifier = modifier,
        onDone = viewModel::complete,
        onLater = viewModel::postpone
    )
}

@Composable
fun HomeReminderSnackbar(
    modifier: Modifier = Modifier,
    viewModel: RemindersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ReminderSnackbar(
        state = state,
        onUndo = viewModel::undoComplete,
        onMessageShown = viewModel::onMessageShown,
        modifier = modifier
    )
}

@Composable
fun HomeReminderSectionContent(
    state: RemindersUiState,
    onDone: (String) -> Unit,
    onLater: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.reminders == null) return

    val reminder = state.nextReminder
    if (reminder == null) {
        if (state.completedReminder == null) EmptyHomeReminderCard(modifier = modifier)
        return
    }

    HomeReminderCard(
        reminder = reminder,
        isBusy = state.busyReminderId == reminder.id,
        errorMessage = state.actionError.takeIf { state.actionErrorReminderId == reminder.id },
        onDone = { onDone(reminder.id) },
        onLater = { onLater(reminder.id) },
        modifier = modifier
    )
}

@Composable
private fun HomeReminderCard(
    reminder: SocialReminder,
    isBusy: Boolean,
    errorMessage: String?,
    onDone: () -> Unit,
    onLater: () -> Unit,
    modifier: Modifier = Modifier
) {
    HomeCard(modifier = modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            if (reminder.isPostponed) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = schedule,
                        contentDescription = "Pospuesto",
                        tint = SereniaPalette.Durazno800,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = reminder.remindAt.toHomeText(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = SereniaPalette.Durazno800
                    )
                }
            } else {
                Text(
                    text = reminder.remindAt.toHomeText(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SereniaPalette.Neutro700
                )
            }
            Text(
                text = reminder.title,
                style = MaterialTheme.typography.headlineSmall,
                color = SereniaPalette.Neutro800,
                modifier = Modifier.semantics { heading() }
            )
        }

        errorMessage?.let { message ->
            AlertBanner(message = message)
        }

        if (reminder.isDue) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                PrimaryButton(
                    text = "Ya lo hice",
                    onClick = onDone,
                    enabled = !isBusy,
                    elevated = false,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = "Más tarde",
                    onClick = onLater,
                    enabled = !isBusy,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun EmptyHomeReminderCard(modifier: Modifier = Modifier) {
    HomeCard(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .background(SereniaPalette.Durazno200, CircleShape)
            ) {
                Icon(
                    imageVector = eventAvailable,
                    contentDescription = null,
                    tint = SereniaPalette.Durazno900,
                    modifier = Modifier.size(32.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Aún no tienes recordatorios",
                    style = MaterialTheme.typography.titleLarge,
                    color = SereniaPalette.Neutro800,
                    modifier = Modifier.semantics { heading() }
                )
                Text(
                    text = "Puedes crear uno en Recordar.",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SereniaPalette.Neutro700
                )
            }
        }
    }
}

@Composable
private fun HomeCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = shape,
                ambientColor = SereniaPalette.Teal700.copy(alpha = 0.08f),
                spotColor = SereniaPalette.Teal700.copy(alpha = 0.08f)
            )
            .background(SereniaPalette.Neutro50, shape)
            .padding(20.dp)
    ) {
        content()
    }
}

private fun ReminderDateTime.toHomeText(): String {
    val article = if (hour % 12 == 1) "a la" else "a las"
    return "${formatRelativeDay(year, month, day)} $article ${formatTime(hour, minute)}"
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun HomeReminderSectionContentPreview() {
    val now = ReminderDateTime.now()
    SereniaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            HomeReminderSectionContent(
                state = RemindersUiState(
                    reminders = listOf(
                        SocialReminder("1", "Llamar a doña Carmen", now, SocialReminderStatus.PRESENTED)
                    )
                ),
                onDone = {},
                onLater = {}
            )
            HomeReminderSectionContent(
                state = RemindersUiState(reminders = emptyList()),
                onDone = {},
                onLater = {}
            )
        }
    }
}
