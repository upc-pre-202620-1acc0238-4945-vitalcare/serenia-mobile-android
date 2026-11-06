package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderDateTime
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminder
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminderStatus
import java.util.Locale

fun SocialReminderDto.toDomain(): SocialReminder {
    return SocialReminder(
        id = this.id,
        title = this.title,
        remindAt = this.remindAt.toReminderDateTime(),
        status = SocialReminderStatus.entries.firstOrNull { it.name == this.status } ?: SocialReminderStatus.SCHEDULED
    )
}

fun ReminderDateTime.toRequestValue(): String {
    return String.format(Locale.ROOT, "%04d-%02d-%02dT%02d:%02d:00", year, month, day, hour, minute)
}

private fun String.toReminderDateTime(): ReminderDateTime {
    return ReminderDateTime(
        year = substring(0, 4).toInt(),
        month = substring(5, 7).toInt(),
        day = substring(8, 10).toInt(),
        hour = substring(11, 13).toInt(),
        minute = substring(14, 16).toInt()
    )
}
