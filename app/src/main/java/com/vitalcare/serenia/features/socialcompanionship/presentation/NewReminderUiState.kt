package com.vitalcare.serenia.features.socialcompanionship.presentation

enum class ReminderDay {
    TODAY,
    TOMORROW,
    OTHER
}

data class ChosenDate(val year: Int, val month: Int, val day: Int)

data class ChosenTime(val hour: Int, val minute: Int)

data class NewReminderUiState(
    val title: String = "",
    val day: ReminderDay? = null,
    val otherDate: ChosenDate? = null,
    val time: ChosenTime? = null,
    val isDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
    val titleError: String? = null,
    val dayError: String? = null,
    val timeError: String? = null,
    val formError: String? = null,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)
