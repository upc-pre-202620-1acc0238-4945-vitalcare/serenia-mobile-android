package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.vitalcare.serenia.features.socialcompanionship.domain.Reminder
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderRepository
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderSchedule
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderStatus
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderType
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import javax.inject.Singleton

// Reminders are kept in memory until the local database is available
@Singleton
class ReminderRepositoryImpl @Inject constructor() : ReminderRepository {

    private val reminders = MutableStateFlow(
        listOf(
            Reminder(
                id = 1,
                title = "Llamar a doña Carmen",
                schedule = "Hoy, 4:00 PM",
                status = ReminderStatus.PENDING,
                isDue = true
            ),
            Reminder(
                id = 2,
                title = "Taller de tejido",
                schedule = "Mañana, 10:00 AM",
                status = ReminderStatus.PENDING,
                isDue = false
            ),
            Reminder(
                id = 3,
                title = "Llamar a Rosario",
                schedule = "Ayer, 5:00 PM",
                status = ReminderStatus.MISSED,
                isDue = false
            )
        )
    )

    private val lastId = AtomicInteger(reminders.value.size)

    override fun getReminders(): Flow<List<Reminder>> {
        return reminders.asStateFlow()
    }

    override suspend fun completeReminder(id: Int): Result<Unit> {
        return updateStatus(id, ReminderStatus.DONE)
    }

    override suspend fun postponeReminder(id: Int): Result<Unit> {
        return updateStatus(id, ReminderStatus.POSTPONED)
    }

    override suspend fun cancelReminder(id: Int): Result<Unit> {
        if (reminders.value.none { reminder -> reminder.id == id }) {
            return Result.failure(NoSuchElementException("Reminder $id not found"))
        }
        reminders.update { currentReminders ->
            currentReminders.filterNot { reminder -> reminder.id == id }
        }
        return Result.success(Unit)
    }

    override suspend fun createReminder(type: ReminderType, schedule: ReminderSchedule): Result<Reminder> {
        val reminder = Reminder(
            id = lastId.incrementAndGet(),
            title = type.label,
            schedule = schedule.label,
            status = ReminderStatus.PENDING,
            isDue = false
        )
        // New reminders always go after the existing ones
        reminders.update { currentReminders -> currentReminders + reminder }
        return Result.success(reminder)
    }

    private fun updateStatus(id: Int, status: ReminderStatus): Result<Unit> {
        if (reminders.value.none { reminder -> reminder.id == id }) {
            return Result.failure(NoSuchElementException("Reminder $id not found"))
        }
        reminders.update { currentReminders ->
            currentReminders.map { reminder ->
                if (reminder.id == id) reminder.copy(status = status) else reminder
            }
        }
        return Result.success(Unit)
    }
}
