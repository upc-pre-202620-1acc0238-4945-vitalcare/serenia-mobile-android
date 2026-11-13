package com.vitalcare.serenia.features.dailycheckin.domain

interface CheckInRepository {

    suspend fun getTodayCheckIn(): Result<CheckIn?>

    suspend fun answerCheckIn(checkInId: String, mood: MoodLevel): Result<CheckIn>
}
