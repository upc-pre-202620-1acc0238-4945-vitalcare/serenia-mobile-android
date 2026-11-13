package com.vitalcare.serenia.features.socialcompanionship.domain

import java.util.Calendar

data class ReminderDateTime(
    val year: Int,
    val month: Int,
    val day: Int,
    val hour: Int,
    val minute: Int
) : Comparable<ReminderDateTime> {

    init {
        require(month in 1..12) { "Invalid month: $month" }
        require(day in 1..31) { "Invalid day: $day" }
        require(hour in 0..23) { "Invalid hour: $hour" }
        require(minute in 0..59) { "Invalid minute: $minute" }
    }

    fun isSameDayAs(other: ReminderDateTime): Boolean {
        return year == other.year && month == other.month && day == other.day
    }

    override fun compareTo(other: ReminderDateTime): Int {
        return compareValuesBy(this, other, { it.year }, { it.month }, { it.day }, { it.hour }, { it.minute })
    }

    companion object {
        fun now(): ReminderDateTime {
            val calendar = Calendar.getInstance()
            return ReminderDateTime(
                year = calendar.get(Calendar.YEAR),
                month = calendar.get(Calendar.MONTH) + 1,
                day = calendar.get(Calendar.DAY_OF_MONTH),
                hour = calendar.get(Calendar.HOUR_OF_DAY),
                minute = calendar.get(Calendar.MINUTE)
            )
        }
    }
}
