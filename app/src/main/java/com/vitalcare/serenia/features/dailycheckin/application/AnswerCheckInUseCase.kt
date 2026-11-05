package com.vitalcare.serenia.features.dailycheckin.application

import com.vitalcare.serenia.features.dailycheckin.domain.CheckIn
import com.vitalcare.serenia.features.dailycheckin.domain.CheckInRepository
import com.vitalcare.serenia.features.dailycheckin.domain.MoodLevel
import javax.inject.Inject

class AnswerCheckInUseCase @Inject constructor(private val repository: CheckInRepository) {

    suspend operator fun invoke(checkInId: String, mood: MoodLevel): Result<CheckIn> {
        return repository.answerCheckIn(checkInId, mood)
    }
}
