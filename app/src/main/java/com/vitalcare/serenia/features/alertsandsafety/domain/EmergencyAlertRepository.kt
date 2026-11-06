package com.vitalcare.serenia.features.alertsandsafety.domain

interface EmergencyAlertRepository {

    suspend fun requestHelp(): Result<EmergencyAlert>
}
