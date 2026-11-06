package com.vitalcare.serenia.features.dailycheckin.application

import com.vitalcare.serenia.features.dailycheckin.domain.CheckIn
import com.vitalcare.serenia.features.dailycheckin.domain.CheckInRepository
import javax.inject.Inject

class GetTodayCheckInUseCase @Inject constructor(private val repository: CheckInRepository) {

    suspend operator fun invoke(): Result<CheckIn?> {
        return repository.getTodayCheckIn()
    }
}
