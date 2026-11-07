package com.vitalcare.serenia.features.iam.infrastructure

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface IamApi {

    @POST("users")
    suspend fun register(@Body request: RegisterUserRequestDto): Response<UserDto>

    @POST("sessions")
    suspend fun signIn(@Body request: SignInRequestDto): Response<AuthenticatedUserDto>

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<UserDto>

    @PUT("users/{userId}/profile")
    suspend fun updateProfile(
        @Path("userId") userId: String,
        @Body request: UpdateProfileRequestDto
    ): Response<UserDto>

    @Multipart
    @PUT("users/{userId}/photo")
    suspend fun uploadPhoto(
        @Path("userId") userId: String,
        @Part file: MultipartBody.Part
    ): Response<UserDto>

    @PUT("users/{userId}/password")
    suspend fun changePassword(
        @Path("userId") userId: String,
        @Body request: ChangePasswordRequestDto
    ): Response<Unit>

    @DELETE("sessions/{sessionId}")
    suspend fun signOut(@Path("sessionId") sessionId: String): Response<Unit>
}
