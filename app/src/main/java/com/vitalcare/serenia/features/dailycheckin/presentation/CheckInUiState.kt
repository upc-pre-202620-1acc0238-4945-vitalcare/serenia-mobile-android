package com.vitalcare.serenia.features.dailycheckin.presentation

import com.vitalcare.serenia.features.dailycheckin.domain.ReminderTime

sealed interface CheckInContent {
    data object Loading : CheckInContent
    data class Pending(val checkInId: String, val question: String) : CheckInContent
    data class Answered(val answer: MoodAnswer) : CheckInContent
    data object Paused : CheckInContent
    data object Missed : CheckInContent
    data class Unavailable(val nextReminderTime: ReminderTime?) : CheckInContent
    data class Failed(val message: String) : CheckInContent
}

data class CheckInUiState(
    val content: CheckInContent = CheckInContent.Loading,
    val isSubmitting: Boolean = false,
    val actionError: String? = null
)
