package com.vitalcare.serenia.features.alertsandsafety.domain

data class EmergencyAlert(
    val id: String,
    val status: AlertStatus
) {
    val familyNotified: Boolean
        get() = status != AlertStatus.DISPATCH_FAILED
}
