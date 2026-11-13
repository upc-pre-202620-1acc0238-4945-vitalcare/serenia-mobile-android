package com.vitalcare.serenia.features.dailycheckin.domain

data class CheckIn(
    val id: String,
    val questionText: String,
    val status: CheckInStatus,
    val paused: Boolean,
    val mood: MoodLevel?
) {
    val canBeAnswered: Boolean
        get() = status == CheckInStatus.PENDING && !paused
}
