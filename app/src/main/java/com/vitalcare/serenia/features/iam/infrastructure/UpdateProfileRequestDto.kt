package com.vitalcare.serenia.features.iam.infrastructure

data class UpdateProfileRequestDto(
    val fullName: String,
    val phoneNumber: String?,
    val birthDate: String?,
    val locale: String,
    val timeZone: String
)
