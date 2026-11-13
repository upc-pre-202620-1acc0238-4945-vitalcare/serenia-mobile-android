package com.vitalcare.serenia.features.dailycheckin.infrastructure

import com.vitalcare.serenia.features.dailycheckin.domain.CheckIn
import com.vitalcare.serenia.features.dailycheckin.domain.CheckInPreferences
import com.vitalcare.serenia.features.dailycheckin.domain.CheckInStatus
import com.vitalcare.serenia.features.dailycheckin.domain.MoodLevel
import com.vitalcare.serenia.features.dailycheckin.domain.ReminderTime
import java.util.Locale

private const val DEFAULT_QUESTION = "¿Cómo amaneciste hoy?"

fun CheckInDto.toDomain(): CheckIn {
    return CheckIn(
        id = this.id,
        questionText = this.questionText ?: DEFAULT_QUESTION,
        status = CheckInStatus.valueOf(this.status),
        paused = this.paused,
        mood = this.mood?.let { MoodLevel.valueOf(it) }
    )
}

fun CheckInPreferencesDto.toDomain(): CheckInPreferences {
    return CheckInPreferences(
        id = this.id,
        reminderTime = this.reminderTime.toReminderTime(),
        simplifiedMode = this.simplifiedMode,
        pausedToday = this.pausedToday
    )
}

fun ReminderTime.toRequestValue(): String {
    return String.format(Locale.ROOT, "%02d:%02d", hour, minute)
}

private fun String.toReminderTime(): ReminderTime {
    val parts = split(":")
    return ReminderTime(hour = parts[0].toInt(), minute = parts[1].toInt())
}
