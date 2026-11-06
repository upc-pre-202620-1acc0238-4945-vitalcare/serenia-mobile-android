package com.vitalcare.serenia.features.carecircle.infrastructure

data class InvitationCodeDto(
    val id: String,
    val code: String,
    val status: String,
    val expiresAt: String
)
