package com.vitalcare.serenia.features.dailycheckin.infrastructure

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface DailyCheckInApi {

    @GET("check-ins/today")
    suspend fun getTodayCheckIn(@Query("olderAdultId") olderAdultId: String): Response<CheckInDto>

    @POST("check-ins/{checkInId}/answer")
    suspend fun answerCheckIn(
        @Path("checkInId") checkInId: String,
        @Body request: AnswerCheckInRequestDto
    ): Response<CheckInDto>

    @GET("check-in-preferences")
    suspend fun getPreferences(@Query("olderAdultId") olderAdultId: String): Response<CheckInPreferencesDto>

    @PUT("check-in-preferences/{preferencesId}/reminder-time")
    suspend fun scheduleCheckIn(
        @Path("preferencesId") preferencesId: String,
        @Body request: ScheduleCheckInRequestDto
    ): Response<CheckInPreferencesDto>

    @POST("check-in-preferences/{preferencesId}/daily-pause")
    suspend fun activateDailyPause(@Path("preferencesId") preferencesId: String): Response<CheckInPreferencesDto>

    @DELETE("check-in-preferences/{preferencesId}/daily-pause")
    suspend fun resumeDailyCheckIn(@Path("preferencesId") preferencesId: String): Response<CheckInPreferencesDto>

    @PUT("check-in-preferences/{preferencesId}/simplified-mode")
    suspend fun updateSimplifiedMode(
        @Path("preferencesId") preferencesId: String,
        @Body request: UpdateSimplifiedModeRequestDto
    ): Response<CheckInPreferencesDto>
}
