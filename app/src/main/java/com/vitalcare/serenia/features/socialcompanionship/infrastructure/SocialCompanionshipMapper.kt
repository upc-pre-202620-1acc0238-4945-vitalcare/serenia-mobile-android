package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import com.vitalcare.serenia.BuildConfig
import com.vitalcare.serenia.features.socialcompanionship.domain.AudioMessage
import com.vitalcare.serenia.features.socialcompanionship.domain.PhotoMessage
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderDateTime
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminder
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminderStatus
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun SocialReminderDto.toDomain(): SocialReminder {
    return SocialReminder(
        id = this.id,
        title = this.title,
        remindAt = this.remindAt.toReminderDateTime(),
        status = SocialReminderStatus.entries.firstOrNull { it.name == this.status } ?: SocialReminderStatus.SCHEDULED
    )
}

fun AudioMessageDto.toDomain(): AudioMessage {
    return AudioMessage(
        id = this.id,
        durationSeconds = this.durationSeconds,
        isShared = this.status == SHARED
    )
}

fun PhotoMessageDto.toDomain(): PhotoMessage {
    return PhotoMessage(
        id = this.id,
        senderId = this.senderId,
        mediaUrl = BuildConfig.API_BASE_URL.substringBefore(API_PATH) + this.mediaUrl,
        sentAtMillis = (this.sentAt ?: this.createdAt).toEpochMillis(),
        isViewed = this.viewed
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

private fun String.toEpochMillis(): Long {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return format.parse(take(19))?.time ?: 0L
}

private const val SHARED = "SHARED"
private const val API_PATH = "/api/"
