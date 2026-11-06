package com.vitalcare.serenia.features.carecircle.application

import com.vitalcare.serenia.features.carecircle.domain.CareCircleRepository
import javax.inject.Inject

class RevokeFamilyLinkUseCase @Inject constructor(private val repository: CareCircleRepository) {

    suspend operator fun invoke(familyLinkId: String): Result<Unit> {
        return repository.revokeFamilyLink(familyLinkId)
    }
}
