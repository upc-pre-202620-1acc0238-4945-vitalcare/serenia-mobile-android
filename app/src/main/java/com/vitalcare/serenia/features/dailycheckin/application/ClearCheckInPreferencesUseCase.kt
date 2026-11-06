package com.vitalcare.serenia.features.dailycheckin.application

import com.vitalcare.serenia.features.dailycheckin.domain.CheckInPreferencesRepository
import javax.inject.Inject

class ClearCheckInPreferencesUseCase @Inject constructor(private val repository: CheckInPreferencesRepository) {

    operator fun invoke() {
        repository.clear()
    }
}
