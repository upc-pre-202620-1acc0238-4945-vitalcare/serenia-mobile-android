package com.vitalcare.serenia.features.dailycheckin.infrastructure

import com.vitalcare.serenia.core.network.toApiException
import com.vitalcare.serenia.core.session.SessionStorage
import com.vitalcare.serenia.features.dailycheckin.domain.CheckIn
import com.vitalcare.serenia.features.dailycheckin.domain.CheckInRepository
import com.vitalcare.serenia.features.dailycheckin.domain.MoodLevel
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckInRepositoryImpl @Inject constructor(
    private val api: DailyCheckInApi,
    private val sessionStorage: SessionStorage
) : CheckInRepository {

    override suspend fun getTodayCheckIn(): Result<CheckIn?> {
        try {
            val userId = sessionStorage.session.first()?.userId
                ?: return Result.failure(IllegalStateException("There is no active session"))
            val response = api.getTodayCheckIn(userId)

            if (response.isSuccessful) {
                response.body()?.let { checkInDto ->
                    return Result.success(checkInDto.toDomain())
                }
            }
            if (response.code() == NOT_FOUND) {
                return Result.success(null)
            }
            return Result.failure(response.toApiException())

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    override suspend fun answerCheckIn(checkInId: String, mood: MoodLevel): Result<CheckIn> {
        try {
            val request = AnswerCheckInRequestDto(mood = mood.name, positiveActivity = null)
            val response = api.answerCheckIn(checkInId, request)

            if (response.isSuccessful) {
                response.body()?.let { checkInDto ->
                    return Result.success(checkInDto.toDomain())
                }
            }
            return Result.failure(response.toApiException())

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    private companion object {
        const val NOT_FOUND = 404
    }
}
