package com.vitalcare.serenia.features.carecircle.infrastructure

import com.vitalcare.serenia.features.carecircle.domain.FamilyLink
import com.vitalcare.serenia.features.carecircle.domain.InvitationCode
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun InvitationCodeDto.toDomain(): InvitationCode {
    return InvitationCode(
        id = this.id,
        code = this.code,
        expiresAtMillis = this.expiresAt.toEpochMillis()
    )
}

fun FamilyLinkDto.toDomain(): FamilyLink {
    return FamilyLink(
        id = this.id,
        relativeId = this.relativeId,
        relationshipLabel = this.relationshipLabel?.takeIf { it.isNotBlank() }
    )
}

private fun String.toEpochMillis(): Long {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ROOT).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    return format.parse(take(19))?.time ?: 0L
}
