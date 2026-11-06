package com.vitalcare.serenia.features.carecircle.application

import com.vitalcare.serenia.features.carecircle.domain.CareCircleRepository
import com.vitalcare.serenia.features.carecircle.domain.InvitationCode
import javax.inject.Inject

class GetInvitationCodeUseCase @Inject constructor(private val repository: CareCircleRepository) {

    suspend operator fun invoke(): Result<InvitationCode> {
        val pendingCodes = repository.getPendingInvitationCodes().getOrElse { return Result.failure(it) }
        val latestCode = pendingCodes.maxByOrNull { it.expiresAtMillis }
        return if (latestCode != null) Result.success(latestCode) else repository.generateInvitationCode()
    }
}
