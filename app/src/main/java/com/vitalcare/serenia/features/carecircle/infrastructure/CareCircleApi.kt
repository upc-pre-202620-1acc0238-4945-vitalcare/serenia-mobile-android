package com.vitalcare.serenia.features.carecircle.infrastructure

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CareCircleApi {

    @GET("care-circles")
    suspend fun getCareCircle(@Query("olderAdultId") olderAdultId: String): Response<CareCircleDto>

    @GET("care-circles/{careCircleId}/invitation-codes")
    suspend fun getPendingInvitationCodes(
        @Path("careCircleId") careCircleId: String
    ): Response<List<InvitationCodeDto>>

    @POST("care-circles/{careCircleId}/invitation-codes")
    suspend fun generateInvitationCode(@Path("careCircleId") careCircleId: String): Response<InvitationCodeDto>

    @GET("care-circles/{careCircleId}/family-links")
    suspend fun getFamilyLinks(@Path("careCircleId") careCircleId: String): Response<List<FamilyLinkDto>>

    @DELETE("care-circles/{careCircleId}/family-links/{familyLinkId}")
    suspend fun revokeFamilyLink(
        @Path("careCircleId") careCircleId: String,
        @Path("familyLinkId") familyLinkId: String
    ): Response<Unit>
}
