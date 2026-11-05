package com.vitalcare.serenia.core.session

data class Session(
    val sessionId: String,
    val token: String,
    val userId: String
)
