package com.vitalcare.serenia.features.carecircle.application

import com.vitalcare.serenia.features.carecircle.domain.CareCircleRepository
import com.vitalcare.serenia.features.carecircle.domain.FamilyLink
import javax.inject.Inject

class GetFamilyLinksUseCase @Inject constructor(private val repository: CareCircleRepository) {

    suspend operator fun invoke(): Result<List<FamilyLink>> {
        return repository.getFamilyLinks()
    }
}
