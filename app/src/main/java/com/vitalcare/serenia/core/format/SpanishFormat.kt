package com.vitalcare.serenia.core.format

import java.util.Calendar

private val WEEKDAYS = listOf("Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado")

private val MONTHS = listOf(
    "enero", "febrero", "marzo", "abril", "mayo", "junio",
    "julio", "agosto", "septiembre", "octubre", "noviembre", "diciembre"
)

fun formatTime(hour: Int, minute: Int): String {
    val period = if (hour < 12) "a. m." else "p. m."
    val displayHour = if (hour % 12 == 0) 12 else hour % 12
    return "$displayHour:${minute.toString().padStart(2, '0')} $period"
}

fun formatLongDate(calendar: Calendar): String {
    val weekday = WEEKDAYS[calendar.get(Calendar.DAY_OF_WEEK) - 1]
    val day = calendar.get(Calendar.DAY_OF_MONTH)
    val month = MONTHS[calendar.get(Calendar.MONTH)]
    return "$weekday $day de $month"
}

fun formatDate(year: Int, month: Int, day: Int): String {
    return "$day de ${MONTHS[month - 1]} de $year"
}

fun formatDayAndMonth(month: Int, day: Int): String {
    return "$day de ${MONTHS[month - 1]}"
}

fun greetingFor(calendar: Calendar): String {
    return when (calendar.get(Calendar.HOUR_OF_DAY)) {
        in 5..11 -> "Buenos días"
        in 12..18 -> "Buenas tardes"
        else -> "Buenas noches"
    }
}
