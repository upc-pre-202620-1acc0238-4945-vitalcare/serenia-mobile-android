package com.vitalcare.serenia.features.iam.infrastructure

data class ChangePasswordRequestDto(
    val currentPassword: String,
    val newPassword: String
)
