package com.vitalcare.serenia.features.alertsandsafety.infrastructure

import com.vitalcare.serenia.core.network.toApiException
import com.vitalcare.serenia.features.alertsandsafety.domain.EmergencyAlert
import com.vitalcare.serenia.features.alertsandsafety.domain.EmergencyAlertRepository
import javax.inject.Inject

class EmergencyAlertRepositoryImpl @Inject constructor(
    private val api: AlertsAndSafetyApi
) : EmergencyAlertRepository {

    override suspend fun requestHelp(): Result<EmergencyAlert> {
        try {
            val response = api.requestHelp()

            if (response.isSuccessful) {
                response.body()?.let { alertDto ->
                    return Result.success(alertDto.toDomain())
                }
            }
            return Result.failure(response.toApiException())

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }
}
