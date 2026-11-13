package com.vitalcare.serenia.features.alertsandsafety.presentation

data class HelpUiState(
    val isSending: Boolean = false,
    val outcome: HelpOutcome? = null
)
