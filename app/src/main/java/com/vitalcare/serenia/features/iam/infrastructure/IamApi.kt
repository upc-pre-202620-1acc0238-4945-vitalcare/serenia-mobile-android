package com.vitalcare.serenia.features.iam.infrastructure

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IamApi {

    @POST("users")
    suspend fun register(@Body request: RegisterUserRequestDto): Response<UserDto>

    @POST("sessions")
    suspend fun signIn(@Body request: SignInRequestDto): Response<AuthenticatedUserDto>

    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<UserDto>
}
