package com.vitalcare.serenia.features.iam.infrastructure

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IamApi {

    @POST("users")
    suspend fun register(@Body request: RegisterUserRequestDto): Response<UserDto>

    @POST("sessions")
    suspend fun signIn(@Body request: SignInRequestDto): Response<AuthenticatedUserDto>
}
