package com.vitalcare.serenia.features.alertsandsafety.infrastructure

import com.vitalcare.serenia.features.alertsandsafety.domain.AlertStatus
import com.vitalcare.serenia.features.alertsandsafety.domain.EmergencyAlert

fun EmergencyAlertDto.toDomain(): EmergencyAlert {
    return EmergencyAlert(
        id = this.id,
        status = AlertStatus.valueOf(this.status)
    )
}
