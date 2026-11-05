package com.vitalcare.serenia.features.alertsandsafety.presentation.alertsent

data class AlertSentUiState(
    val contactNames: List<String> = emptyList(),
    val message: String = ""
)
