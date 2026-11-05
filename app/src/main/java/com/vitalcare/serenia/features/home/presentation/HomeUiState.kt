package com.vitalcare.serenia.features.home.presentation

import com.vitalcare.serenia.features.dailycheckin.domain.Mood

data class HomeUiState(
    val userName: String = "",
    val greeting: String = "",
    val formattedDate: String = "",
    val selectedMood: Mood? = null,
    val hasSkippedToday: Boolean = false
)
