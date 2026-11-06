package com.vitalcare.serenia.features.dailycheckin.domain

class ReminderTimeOutOfRangeException : IllegalArgumentException("The reminder time must be between 06:00 and 20:00")
