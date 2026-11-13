package com.vitalcare.serenia.features.carecircle.application

import com.vitalcare.serenia.features.carecircle.domain.CareCircleRepository
import com.vitalcare.serenia.features.carecircle.domain.InvitationCode
import javax.inject.Inject

class GenerateInvitationCodeUseCase @Inject constructor(private val repository: CareCircleRepository) {

    suspend operator fun invoke(): Result<InvitationCode> {
        return repository.generateInvitationCode()
    }
}
