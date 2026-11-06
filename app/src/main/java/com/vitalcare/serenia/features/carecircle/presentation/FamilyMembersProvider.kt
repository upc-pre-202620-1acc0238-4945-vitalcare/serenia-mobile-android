package com.vitalcare.serenia.features.carecircle.presentation

import com.vitalcare.serenia.features.carecircle.application.GetFamilyLinksUseCase
import com.vitalcare.serenia.features.iam.application.GetUserUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FamilyMembersProvider @Inject constructor(
    private val getFamilyLinks: GetFamilyLinksUseCase,
    private val getUser: GetUserUseCase
) {

    suspend operator fun invoke(): Result<List<FamilyMember>> {
        val familyLinks = getFamilyLinks().getOrElse { return Result.failure(it) }

        val members = coroutineScope {
            familyLinks.map { familyLink ->
                async {
                    FamilyMember(
                        familyLinkId = familyLink.id,
                        relativeId = familyLink.relativeId,
                        fullName = getUser(familyLink.relativeId).getOrNull()?.fullName ?: UNKNOWN_NAME,
                        relationshipLabel = familyLink.relationshipLabel
                    )
                }
            }.awaitAll()
        }
        return Result.success(members)
    }

    private companion object {
        const val UNKNOWN_NAME = "Familiar"
    }
}
