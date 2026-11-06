package com.vitalcare.serenia.features.alertsandsafety.infrastructure

import retrofit2.Response
import retrofit2.http.POST

interface AlertsAndSafetyApi {

    @POST("emergency-alerts")
    suspend fun requestHelp(): Response<EmergencyAlertDto>
}
