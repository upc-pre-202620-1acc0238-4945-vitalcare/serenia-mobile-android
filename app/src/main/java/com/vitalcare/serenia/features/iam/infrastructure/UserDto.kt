package com.vitalcare.serenia.features.iam.infrastructure

data class UserDto(
    val id: String,
    val email: String,
    val role: String,
    val fullName: String,
    val phoneNumber: String?,
    val birthDate: String?,
    val photoUrl: String?,
    val locale: String,
    val timeZone: String,
    val status: String
)
