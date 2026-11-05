package com.vitalcare.serenia.features.iam.infrastructure

data class AuthenticatedUserDto(
    val sessionId: String,
    val token: String,
    val expiresAt: String,
    val user: UserDto
)
