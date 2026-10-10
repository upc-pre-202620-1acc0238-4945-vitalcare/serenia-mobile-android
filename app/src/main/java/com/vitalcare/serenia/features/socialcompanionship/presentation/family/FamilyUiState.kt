package com.vitalcare.serenia.features.socialcompanionship.presentation.family

import com.vitalcare.serenia.features.socialcompanionship.domain.FamilyPhoto

data class FamilyUiState(
    val photos: List<FamilyPhoto> = emptyList()
)
