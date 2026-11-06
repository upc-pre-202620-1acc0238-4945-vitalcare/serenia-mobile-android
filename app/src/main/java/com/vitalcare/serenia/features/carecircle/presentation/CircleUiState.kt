package com.vitalcare.serenia.features.carecircle.presentation

import com.vitalcare.serenia.features.carecircle.domain.InvitationCode

data class FamilyMember(
    val familyLinkId: String,
    val fullName: String,
    val relationshipLabel: String?
) {
    val firstName: String
        get() = fullName.trim().substringBefore(" ")

    val initials: String
        get() = fullName.trim().split(" ").filter { it.isNotBlank() }.take(2)
            .joinToString("") { it.first().uppercase() }

    val relationship: String
        get() = relationshipLabel?.let { label ->
            if (label.startsWith("tu ", ignoreCase = true)) label.replaceFirstChar { it.uppercase() } else "Tu $label"
        } ?: "Tu familiar"
}

data class CircleUiState(
    val invitationCode: InvitationCode? = null,
    val isGeneratingCode: Boolean = false,
    val codeError: String? = null,
    val members: List<FamilyMember>? = null,
    val membersError: String? = null,
    val memberToRemove: String? = null,
    val isRemoving: Boolean = false,
    val removeError: String? = null
)
