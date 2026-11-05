package com.vitalcare.serenia.features.carecircle.presentation

import com.vitalcare.serenia.features.carecircle.domain.CircleMember

data class CircleUiState(
    val invitationCode: String = "",
    val members: List<CircleMember> = emptyList(),
    val memberPendingRemovalId: Int? = null
)

// One-time events that the screen shows once and then forgets
sealed interface CircleEvent {
    data object CodeCopied : CircleEvent
    data object CodeRenewed : CircleEvent
    data class MemberRemoved(val name: String) : CircleEvent
}
