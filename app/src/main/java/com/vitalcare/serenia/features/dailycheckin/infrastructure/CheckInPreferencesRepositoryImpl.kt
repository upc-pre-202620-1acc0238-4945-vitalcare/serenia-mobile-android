package com.vitalcare.serenia.features.dailycheckin.infrastructure

import com.vitalcare.serenia.core.network.toApiException
import com.vitalcare.serenia.core.session.SessionStorage
import com.vitalcare.serenia.features.dailycheckin.domain.CheckInPreferences
import com.vitalcare.serenia.features.dailycheckin.domain.CheckInPreferencesRepository
import com.vitalcare.serenia.features.dailycheckin.domain.ReminderTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckInPreferencesRepositoryImpl @Inject constructor(
    private val api: DailyCheckInApi,
    private val sessionStorage: SessionStorage
) : CheckInPreferencesRepository {

    private val _preferences = MutableStateFlow<CheckInPreferences?>(null)
    override val preferences: StateFlow<CheckInPreferences?> = _preferences.asStateFlow()

    override suspend fun getPreferences(): Result<CheckInPreferences> {
        try {
            val userId = sessionStorage.session.first()?.userId
                ?: return Result.failure(IllegalStateException("There is no active session"))
            return handle(api.getPreferences(userId))

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    override suspend fun scheduleCheckIn(reminderTime: ReminderTime): Result<CheckInPreferences> {
        return change { preferencesId ->
            api.scheduleCheckIn(preferencesId, ScheduleCheckInRequestDto(reminderTime.toRequestValue()))
        }
    }

    override suspend fun activateDailyPause(): Result<CheckInPreferences> {
        return change { preferencesId -> api.activateDailyPause(preferencesId) }
    }

    override suspend fun resumeDailyCheckIn(): Result<CheckInPreferences> {
        return change { preferencesId -> api.resumeDailyCheckIn(preferencesId) }
    }

    override suspend fun updateSimplifiedMode(enabled: Boolean): Result<CheckInPreferences> {
        return change { preferencesId ->
            api.updateSimplifiedMode(preferencesId, UpdateSimplifiedModeRequestDto(enabled))
        }
    }

    override fun clear() {
        _preferences.value = null
    }

    private suspend fun change(
        request: suspend (preferencesId: String) -> Response<CheckInPreferencesDto>
    ): Result<CheckInPreferences> {
        try {
            val preferencesId = _preferences.value?.id
                ?: getPreferences().getOrElse { return Result.failure(it) }.id
            return handle(request(preferencesId))

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    private fun handle(response: Response<CheckInPreferencesDto>): Result<CheckInPreferences> {
        if (response.isSuccessful) {
            response.body()?.let { preferencesDto ->
                val preferences = preferencesDto.toDomain()
                _preferences.value = preferences
                return Result.success(preferences)
            }
        }
        return Result.failure(response.toApiException())
    }
}
