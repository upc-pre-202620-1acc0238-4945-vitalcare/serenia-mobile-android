package com.vitalcare.serenia.features.iam.infrastructure

data class RegisterUserRequestDto(
    val email: String,
    val password: String,
    val role: String,
    val fullName: String,
    val phoneNumber: String?,
    val birthDate: String?,
    val locale: String,
    val timeZone: String
)
