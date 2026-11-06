package com.vitalcare.serenia.features.iam.domain

data class User(
    val id: String,
    val email: String,
    val role: UserRole,
    val fullName: String,
    val phoneNumber: String?,
    val birthDate: BirthDate?,
    val photoUrl: String?,
    val locale: String,
    val timeZone: String
)
