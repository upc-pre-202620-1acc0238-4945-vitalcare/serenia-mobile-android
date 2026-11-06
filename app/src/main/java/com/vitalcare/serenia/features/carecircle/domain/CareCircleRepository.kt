package com.vitalcare.serenia.features.carecircle.domain

interface CareCircleRepository {

    suspend fun getCareCircleId(): Result<String>

    suspend fun getPendingInvitationCodes(): Result<List<InvitationCode>>

    suspend fun generateInvitationCode(): Result<InvitationCode>

    suspend fun getFamilyLinks(): Result<List<FamilyLink>>

    suspend fun revokeFamilyLink(familyLinkId: String): Result<Unit>
}
