package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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

    @Multipart
    @POST("care-circles/{careCircleId}/audio-messages")
    suspend fun recordAudioMessage(
        @Path("careCircleId") careCircleId: String,
        @Part file: MultipartBody.Part,
        @Part("durationSeconds") durationSeconds: RequestBody
    ): Response<AudioMessageDto>

    @POST("care-circles/{careCircleId}/audio-messages/{audioMessageId}/share")
    suspend fun shareAudioMessage(
        @Path("careCircleId") careCircleId: String,
        @Path("audioMessageId") audioMessageId: String
    ): Response<AudioMessageDto>

    @DELETE("care-circles/{careCircleId}/audio-messages/{audioMessageId}")
    suspend fun discardAudioMessage(
        @Path("careCircleId") careCircleId: String,
        @Path("audioMessageId") audioMessageId: String
    ): Response<Unit>

    @GET("care-circles/{careCircleId}/photo-messages")
    suspend fun getPhotoMessages(@Path("careCircleId") careCircleId: String): Response<List<PhotoMessageDto>>

    @POST("care-circles/{careCircleId}/photo-messages/{photoMessageId}/view")
    suspend fun viewPhotoMessage(
        @Path("careCircleId") careCircleId: String,
        @Path("photoMessageId") photoMessageId: String
    ): Response<PhotoMessageDto>
}
