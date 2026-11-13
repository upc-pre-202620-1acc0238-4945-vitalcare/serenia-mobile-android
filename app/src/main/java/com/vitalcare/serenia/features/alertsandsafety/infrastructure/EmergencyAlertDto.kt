package com.vitalcare.serenia.features.alertsandsafety.infrastructure

data class EmergencyAlertDto(
    val id: String,
    val olderAdultId: String,
    val type: String,
    val status: String,
    val triggeredAt: String,
    val deliveryConfirmed: Boolean
)
