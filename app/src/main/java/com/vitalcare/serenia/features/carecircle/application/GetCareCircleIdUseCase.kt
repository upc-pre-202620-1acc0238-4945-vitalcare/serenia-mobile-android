package com.vitalcare.serenia.features.carecircle.application

import com.vitalcare.serenia.features.carecircle.domain.CareCircleRepository
import javax.inject.Inject

class GetCareCircleIdUseCase @Inject constructor(private val repository: CareCircleRepository) {

    suspend operator fun invoke(): Result<String> {
        return repository.getCareCircleId()
    }
}
