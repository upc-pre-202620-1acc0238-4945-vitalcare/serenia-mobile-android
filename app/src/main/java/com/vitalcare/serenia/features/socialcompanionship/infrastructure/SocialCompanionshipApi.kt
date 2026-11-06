package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SocialCompanionshipApi {

    @GET("social-reminders")
    suspend fun getActiveReminders(@Query("olderAdultId") olderAdultId: String): Response<List<SocialReminderDto>>

    @POST("social-reminders")
    suspend fun scheduleReminder(@Body request: ScheduleReminderRequestDto): Response<SocialReminderDto>

    @POST("social-reminders/{reminderId}/postpone")
    suspend fun postponeReminder(@Path("reminderId") reminderId: String): Response<SocialReminderDto>

    @POST("social-reminders/{reminderId}/complete")
    suspend fun completeReminder(@Path("reminderId") reminderId: String): Response<SocialReminderDto>

    @POST("social-reminders/{reminderId}/cancel")
    suspend fun cancelReminder(@Path("reminderId") reminderId: String): Response<SocialReminderDto>
}
