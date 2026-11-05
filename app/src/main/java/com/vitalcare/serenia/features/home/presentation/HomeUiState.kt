package com.vitalcare.serenia.features.home.presentation.home

import com.vitalcare.serenia.features.checkin.domain.Mood

data class HomeUiState(
    val userName: String = "",
    val greeting: String = "",
    val formattedDate: String = "",
    val selectedMood: Mood? = null,
    val hasSkippedToday: Boolean = false
)
