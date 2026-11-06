package com.vitalcare.serenia.features.socialcompanionship.domain

data class SocialReminder(
    val id: String,
    val title: String,
    val remindAt: ReminderDateTime,
    val status: SocialReminderStatus
) {
    val isDue: Boolean
        get() = status == SocialReminderStatus.PRESENTED

    val isPostponed: Boolean
        get() = status == SocialReminderStatus.POSTPONED

    val isCall: Boolean
        get() = CALL_WORDS.any { word -> title.contains(word, ignoreCase = true) }

    private companion object {
        val CALL_WORDS = listOf("llamar", "llamada", "llámale", "telefonear")
    }
}
