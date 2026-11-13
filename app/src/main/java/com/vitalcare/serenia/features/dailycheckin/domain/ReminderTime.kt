package com.vitalcare.serenia.features.dailycheckin.domain

data class ReminderTime(val hour: Int, val minute: Int) {

    init {
        require(hour in 0..23) { "Invalid hour: $hour" }
        require(minute in 0..59) { "Invalid minute: $minute" }
    }

    val isWithinAllowedRange: Boolean
        get() = minutesOfDay in EARLIEST.minutesOfDay..LATEST.minutesOfDay

    private val minutesOfDay: Int
        get() = hour * 60 + minute

    companion object {
        val EARLIEST = ReminderTime(6, 0)
        val LATEST = ReminderTime(20, 0)
        val DEFAULT = ReminderTime(10, 0)
    }
}
