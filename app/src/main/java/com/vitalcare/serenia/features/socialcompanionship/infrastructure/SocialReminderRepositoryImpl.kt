package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import com.vitalcare.serenia.core.network.toApiException
import com.vitalcare.serenia.core.session.SessionStorage
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderDateTime
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminder
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminderRepository
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject

class SocialReminderRepositoryImpl @Inject constructor(
    private val api: SocialCompanionshipApi,
    private val sessionStorage: SessionStorage
) : SocialReminderRepository {

    override suspend fun getActiveReminders(): Result<List<SocialReminder>> {
        try {
            val userId = sessionStorage.session.first()?.userId
                ?: return Result.failure(IllegalStateException("There is no active session"))
            return api.getActiveReminders(userId).toResult { reminders -> reminders.map { it.toDomain() } }

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    override suspend fun scheduleReminder(title: String, remindAt: ReminderDateTime): Result<SocialReminder> {
        return request {
            val dto = ScheduleReminderRequestDto(title = title, description = null, remindAt = remindAt.toRequestValue())
            api.scheduleReminder(dto)
        }
    }

    override suspend fun postponeReminder(reminderId: String): Result<SocialReminder> {
        return request { api.postponeReminder(reminderId) }
    }

    override suspend fun completeReminder(reminderId: String): Result<SocialReminder> {
        return request { api.completeReminder(reminderId) }
    }

    override suspend fun cancelReminder(reminderId: String): Result<SocialReminder> {
        return request { api.cancelReminder(reminderId) }
    }

    private suspend fun request(call: suspend () -> Response<SocialReminderDto>): Result<SocialReminder> {
        try {
            return call().toResult { reminder -> reminder.toDomain() }

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    private fun <D, T> Response<D>.toResult(transform: (D) -> T): Result<T> {
        if (isSuccessful) {
            body()?.let { dto ->
                return Result.success(transform(dto))
            }
        }
        return Result.failure(toApiException())
    }
}
