package com.vitalcare.serenia.features.carecircle.infrastructure

data class FamilyLinkDto(
    val id: String,
    val careCircleId: String,
    val relativeId: String,
    val relationshipLabel: String?,
    val status: String,
    val linkedAt: String
)
