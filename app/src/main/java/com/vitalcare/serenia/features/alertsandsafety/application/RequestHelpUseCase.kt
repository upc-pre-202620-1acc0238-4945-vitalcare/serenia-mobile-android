package com.vitalcare.serenia.features.alertsandsafety.application

import com.vitalcare.serenia.features.alertsandsafety.domain.EmergencyAlert
import com.vitalcare.serenia.features.alertsandsafety.domain.EmergencyAlertRepository
import javax.inject.Inject

class RequestHelpUseCase @Inject constructor(private val repository: EmergencyAlertRepository) {

    suspend operator fun invoke(): Result<EmergencyAlert> {
        return repository.requestHelp()
    }
}
