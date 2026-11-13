package com.vitalcare.serenia.features.iam.infrastructure

data class SignInRequestDto(
    val email: String,
    val password: String,
    val deviceInfo: String?
)
