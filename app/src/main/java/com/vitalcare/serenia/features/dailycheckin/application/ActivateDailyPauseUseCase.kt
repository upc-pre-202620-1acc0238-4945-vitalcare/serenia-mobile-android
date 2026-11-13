package com.vitalcare.serenia.features.dailycheckin.application

import com.vitalcare.serenia.features.dailycheckin.domain.CheckInPreferences
import com.vitalcare.serenia.features.dailycheckin.domain.CheckInPreferencesRepository
import javax.inject.Inject

class ActivateDailyPauseUseCase @Inject constructor(private val repository: CheckInPreferencesRepository) {

    suspend operator fun invoke(): Result<CheckInPreferences> {
        return repository.activateDailyPause()
    }
}
