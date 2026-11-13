package com.vitalcare.serenia.features.dailycheckin.application

import com.vitalcare.serenia.features.dailycheckin.domain.CheckInPreferences
import com.vitalcare.serenia.features.dailycheckin.domain.CheckInPreferencesRepository
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ObserveCheckInPreferencesUseCase @Inject constructor(private val repository: CheckInPreferencesRepository) {

    operator fun invoke(): StateFlow<CheckInPreferences?> {
        return repository.preferences
    }
}
