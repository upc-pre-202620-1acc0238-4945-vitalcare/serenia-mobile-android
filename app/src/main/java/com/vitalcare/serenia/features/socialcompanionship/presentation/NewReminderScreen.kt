package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.calendarToday
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.BackLink
import com.vitalcare.serenia.core.designsystems.components.ChosenValueCard
import com.vitalcare.serenia.core.designsystems.components.ChosenValueTone
import com.vitalcare.serenia.core.designsystems.components.DateInputDialog
import com.vitalcare.serenia.core.designsystems.components.FieldError
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.SecondaryButton
import com.vitalcare.serenia.core.designsystems.components.SereniaTextField
import com.vitalcare.serenia.core.designsystems.components.TimeInputDialog
import com.vitalcare.serenia.core.designsystems.edit
import com.vitalcare.serenia.core.designsystems.schedule
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystems.theme.SereniaTouch
import com.vitalcare.serenia.core.format.formatLongDate
import com.vitalcare.serenia.core.format.formatTime
import java.util.Calendar

@Composable
fun NewReminderScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewReminderViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            viewModel.onSavedHandled()
            onSaved()
        }
    }

    NewReminderContent(
        state = state,
        modifier = modifier,
        onBack = onBack,
        onTitleChange = viewModel::onTitleChange,
        onDayClick = viewModel::chooseDay,
        onChangeDate = viewModel::openDatePicker,
        onChangeTime = viewModel::openTimePicker,
        onSave = viewModel::save
    )

    if (state.isDatePickerVisible) {
        val initialDate = state.otherDate ?: defaultOtherDate()
        DateInputDialog(
            title = "¿Qué día?",
            initialYear = initialDate.year,
            initialMonth = initialDate.month,
            initialDay = initialDate.day,
            onConfirm = viewModel::onDateChosen,
            onDismiss = viewModel::closeDatePicker,
            upcoming = true
        )
    }

    if (state.isTimePickerVisible) {
        val initialTime = state.time ?: defaultTime()
        TimeInputDialog(
            title = "¿A qué hora?",
            initialHour = initialTime.hour,
            initialMinute = initialTime.minute,
            onConfirm = viewModel::onTimeChosen,
            onDismiss = viewModel::closeTimePicker
        )
    }
}

@Composable
fun NewReminderContent(
    state: NewReminderUiState,
    onBack: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDayClick: (ReminderDay) -> Unit,
    onChangeDate: () -> Unit,
    onChangeTime: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
            .verticalScroll(rememberScrollState())
            .padding(start = SereniaSpacing.screen, top = 12.dp, end = SereniaSpacing.screen, bottom = 24.dp)
    ) {
        Column {
            BackLink(onClick = onBack, text = "Recordatorios")
            Text(
                text = "Nuevo recordatorio",
                style = MaterialTheme.typography.headlineLarge,
                color = SereniaPalette.Teal700,
                modifier = Modifier.semantics { heading() }
            )
        }

        SereniaTextField(
            label = "¿Qué quieres recordar?",
            value = state.title,
            onValueChange = onTitleChange,
            placeholder = "Ej. Llamar a doña Carmen",
            error = state.titleError,
            maxLength = TITLE_MAX_LENGTH,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            labelContent = {
                QuestionLabel(
                    text = "¿Qué quieres recordar?",
                    icon = edit,
                    background = SereniaPalette.Durazno200,
                    tint = SereniaPalette.Durazno900
                )
            }
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            QuestionLabel(
                text = "¿Qué día?",
                icon = calendarToday,
                background = SereniaPalette.Verde200,
                tint = SereniaPalette.Verde900
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.selectableGroup()
            ) {
                DayButton(
                    text = "Hoy",
                    isSelected = state.day == ReminderDay.TODAY,
                    onClick = { onDayClick(ReminderDay.TODAY) },
                    modifier = Modifier.weight(1f)
                )
                DayButton(
                    text = "Mañana",
                    isSelected = state.day == ReminderDay.TOMORROW,
                    onClick = { onDayClick(ReminderDay.TOMORROW) },
                    modifier = Modifier.weight(1f)
                )
                DayButton(
                    text = "Otro día",
                    isSelected = state.day == ReminderDay.OTHER,
                    onClick = { onDayClick(ReminderDay.OTHER) },
                    modifier = Modifier.weight(1f)
                )
            }
            val otherDate = state.otherDate
            if (state.day == ReminderDay.OTHER && otherDate != null) {
                ChosenValueCard(
                    label = "Día elegido",
                    value = formatLongDate(otherDate.toCalendar()),
                    onChange = onChangeDate,
                    tone = ChosenValueTone.Verde,
                    compact = true
                )
            }
            state.dayError?.let { message ->
                FieldError(message = message)
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            QuestionLabel(
                text = "¿A qué hora?",
                icon = schedule,
                background = SereniaPalette.Teal100,
                tint = SereniaPalette.Teal800
            )
            val time = state.time
            if (time == null) {
                SecondaryButton(
                    text = "Elegir hora",
                    icon = schedule,
                    onClick = onChangeTime
                )
            } else {
                ChosenValueCard(
                    label = "Hora elegida",
                    value = formatTime(time.hour, time.minute),
                    onChange = onChangeTime,
                    isError = state.timeError != null,
                    errorMessage = state.timeError,
                    compact = true
                )
            }
            state.timeError?.let { message ->
                FieldError(message = message)
            }
        }

        state.formError?.let { message ->
            AlertBanner(message = message)
        }

        PrimaryButton(
            text = "Guardar recordatorio",
            onClick = onSave,
            isLoading = state.isSaving
        )
    }
}

@Composable
private fun QuestionLabel(text: String, icon: ImageVector, background: Color, tint: Color) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .background(background, CircleShape)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = SereniaPalette.Neutro800,
            modifier = Modifier.semantics { heading() }
        )
    }
}

@Composable
private fun DayButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(SereniaRadius.sm)
    val background = if (isSelected) SereniaPalette.Teal700 else SereniaPalette.Neutro50
    val border = if (isSelected) SereniaPalette.Teal700 else SereniaPalette.Neutro300

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .heightIn(min = SereniaTouch.senior)
            .clip(shape)
            .background(background, shape)
            .border(BorderStroke(2.dp, border), shape)
            .selectable(selected = isSelected, role = Role.RadioButton, onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 12.dp)
    ) {
        Text(
            text = text,
            fontFamily = Nunito,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center,
            color = if (isSelected) SereniaPalette.Neutro50 else SereniaPalette.Neutro800
        )
    }
}

private fun ChosenDate.toCalendar(): Calendar {
    return Calendar.getInstance().apply {
        clear()
        set(year, month - 1, day)
    }
}

private fun defaultOtherDate(): ChosenDate {
    val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 2) }
    return ChosenDate(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH) + 1,
        day = calendar.get(Calendar.DAY_OF_MONTH)
    )
}

private fun defaultTime(): ChosenTime {
    val calendar = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }
    return ChosenTime(hour = calendar.get(Calendar.HOUR_OF_DAY), minute = 0)
}

private const val TITLE_MAX_LENGTH = 120

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun NewReminderContentPreview() {
    SereniaTheme {
        NewReminderContent(
            state = NewReminderUiState(
                title = "Llamar a Rosario",
                day = ReminderDay.OTHER,
                otherDate = ChosenDate(2026, 11, 7),
                time = ChosenTime(9, 0)
            ),
            onBack = {},
            onTitleChange = {},
            onDayClick = {},
            onChangeDate = {},
            onChangeTime = {},
            onSave = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun NewReminderErrorContentPreview() {
    SereniaTheme {
        NewReminderContent(
            state = NewReminderUiState(
                day = ReminderDay.TODAY,
                time = ChosenTime(14, 30),
                titleError = "Escribe qué quieres recordar.",
                timeError = "Esa hora ya pasó. Elige una más tarde."
            ),
            onBack = {},
            onTitleChange = {},
            onDayClick = {},
            onChangeDate = {},
            onChangeTime = {},
            onSave = {}
        )
    }
}
