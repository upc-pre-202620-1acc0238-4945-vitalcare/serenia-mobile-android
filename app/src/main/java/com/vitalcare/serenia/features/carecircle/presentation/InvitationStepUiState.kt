package com.vitalcare.serenia.features.carecircle.presentation

import com.vitalcare.serenia.features.carecircle.domain.InvitationCode

data class InvitationStepUiState(
    val invitationCode: InvitationCode? = null,
    val error: String? = null
)
