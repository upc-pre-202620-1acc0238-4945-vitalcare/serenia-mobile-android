package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.socialcompanionship.application.ScheduleReminderUseCase
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderDateTime
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderTimeInPastException
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderTitleRequiredException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class NewReminderViewModel @Inject constructor(
    private val scheduleReminder: ScheduleReminderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(NewReminderUiState())
    val state: StateFlow<NewReminderUiState> = _state.asStateFlow()

    fun onTitleChange(title: String) {
        _state.update { it.copy(title = title, titleError = null, formError = null) }
    }

    fun chooseDay(day: ReminderDay) {
        if (day == ReminderDay.OTHER) {
            _state.update { it.copy(isDatePickerVisible = true) }
            return
        }
        _state.update { it.copy(day = day, dayError = null, timeError = null, formError = null) }
    }

    fun openDatePicker() {
        _state.update { it.copy(isDatePickerVisible = true) }
    }

    fun closeDatePicker() {
        _state.update { it.copy(isDatePickerVisible = false) }
    }

    fun onDateChosen(year: Int, month: Int, day: Int) {
        _state.update {
            it.copy(
                day = ReminderDay.OTHER,
                otherDate = ChosenDate(year, month, day),
                isDatePickerVisible = false,
                dayError = null,
                timeError = null,
                formError = null
            )
        }
    }

    fun openTimePicker() {
        _state.update { it.copy(isTimePickerVisible = true) }
    }

    fun closeTimePicker() {
        _state.update { it.copy(isTimePickerVisible = false) }
    }

    fun onTimeChosen(hour: Int, minute: Int) {
        _state.update {
            it.copy(time = ChosenTime(hour, minute), isTimePickerVisible = false, timeError = null, formError = null)
        }
    }

    fun save() {
        val current = _state.value
        if (current.isSaving) return

        val titleError = if (current.title.isBlank()) TITLE_REQUIRED else null
        val date = current.chosenDate()
        val dayError = if (date == null) DAY_REQUIRED else null
        val time = current.time
        val timeError = if (time == null) TIME_REQUIRED else null
        if (titleError != null || dayError != null || timeError != null || date == null || time == null) {
            _state.update { it.copy(titleError = titleError, dayError = dayError, timeError = timeError) }
            return
        }

        val remindAt = ReminderDateTime(date.year, date.month, date.day, time.hour, time.minute)
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isSaving = true, formError = null) }

            scheduleReminder(current.title, remindAt).fold(
                onSuccess = {
                    _state.update { it.copy(isSaving = false, isSaved = true) }
                },
                onFailure = { exception ->
                    _state.update {
                        when (exception) {
                            is ReminderTitleRequiredException -> it.copy(isSaving = false, titleError = TITLE_REQUIRED)
                            is ReminderTimeInPastException -> it.copy(isSaving = false, timeError = TIME_IN_PAST)
                            else -> it.copy(isSaving = false, formError = exception.toUserMessage())
                        }
                    }
                }
            )
        }
    }

    fun onSavedHandled() {
        _state.update { it.copy(isSaved = false) }
    }

    private fun NewReminderUiState.chosenDate(): ChosenDate? {
        return when (day) {
            ReminderDay.TODAY -> dateFromToday(0)
            ReminderDay.TOMORROW -> dateFromToday(1)
            ReminderDay.OTHER -> otherDate
            null -> null
        }
    }

    private fun dateFromToday(days: Int): ChosenDate {
        val calendar = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, days) }
        return ChosenDate(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH) + 1,
            day = calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    private companion object {
        const val TITLE_REQUIRED = "Escribe qué quieres recordar."
        const val DAY_REQUIRED = "Elige qué día."
        const val TIME_REQUIRED = "Elige a qué hora."
        const val TIME_IN_PAST = "Esa hora ya pasó. Elige una más tarde."
    }
}
