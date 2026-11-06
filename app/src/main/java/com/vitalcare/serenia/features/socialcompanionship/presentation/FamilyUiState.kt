package com.vitalcare.serenia.features.socialcompanionship.presentation

import com.vitalcare.serenia.core.format.joinNames
import com.vitalcare.serenia.features.carecircle.presentation.FamilyMember
import com.vitalcare.serenia.features.socialcompanionship.domain.PhotoMessage

data class PhotoItem(
    val photo: PhotoMessage,
    val senderName: String
)

data class FamilyUiState(
    val members: List<FamilyMember>? = null,
    val photoMessages: List<PhotoMessage>? = null,
    val loadError: String? = null,
    val message: String? = null
) {
    val photos: List<PhotoItem>?
        get() = photoMessages?.map { photo ->
            PhotoItem(
                photo = photo,
                senderName = members?.firstOrNull { it.relativeId == photo.senderId }?.firstName ?: UNKNOWN_SENDER
            )
        }

    val newPhotos: List<PhotoItem>
        get() = photos.orEmpty().filterNot { it.photo.isViewed }

    val membersText: String
        get() = members.orEmpty().takeIf { it.isNotEmpty() }
            ?.let { members -> joinNames(members.map { it.firstName }, conjunction = "o") }
            ?: "tu familia"

    private companion object {
        const val UNKNOWN_SENDER = "Tu familiar"
    }
}
