package com.vitalcare.serenia.features.dailycheckin.infrastructure

data class AnswerCheckInRequestDto(
    val mood: String,
    val positiveActivity: String?
)
