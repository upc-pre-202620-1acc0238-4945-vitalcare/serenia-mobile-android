package com.vitalcare.serenia.features.socialcompanionship.presentation

import com.vitalcare.serenia.features.socialcompanionship.domain.FamilyPhoto

data class FamilyUiState(
    val photos: List<FamilyPhoto> = emptyList()
)
