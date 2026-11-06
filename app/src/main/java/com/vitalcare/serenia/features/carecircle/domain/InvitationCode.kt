package com.vitalcare.serenia.features.carecircle.domain

data class InvitationCode(
    val id: String,
    val code: String,
    val expiresAtMillis: Long
)
