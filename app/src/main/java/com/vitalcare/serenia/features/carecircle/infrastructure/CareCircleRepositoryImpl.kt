package com.vitalcare.serenia.features.carecircle.infrastructure

import com.vitalcare.serenia.core.network.toApiException
import com.vitalcare.serenia.core.session.SessionStorage
import com.vitalcare.serenia.features.carecircle.domain.CareCircleRepository
import com.vitalcare.serenia.features.carecircle.domain.FamilyLink
import com.vitalcare.serenia.features.carecircle.domain.InvitationCode
import kotlinx.coroutines.flow.first
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CareCircleRepositoryImpl @Inject constructor(
    private val api: CareCircleApi,
    private val sessionStorage: SessionStorage
) : CareCircleRepository {

    private var cachedCareCircle: Pair<String, String>? = null

    override suspend fun getPendingInvitationCodes(): Result<List<InvitationCode>> {
        return request { careCircleId ->
            api.getPendingInvitationCodes(careCircleId).toResult { codes ->
                codes.filter { it.status == PENDING }.map { it.toDomain() }
            }
        }
    }

    override suspend fun generateInvitationCode(): Result<InvitationCode> {
        return request { careCircleId ->
            api.generateInvitationCode(careCircleId).toResult { code -> code.toDomain() }
        }
    }

    override suspend fun getFamilyLinks(): Result<List<FamilyLink>> {
        return request { careCircleId ->
            api.getFamilyLinks(careCircleId).toResult { links ->
                links.filter { it.status == ACTIVE }.map { it.toDomain() }
            }
        }
    }

    override suspend fun revokeFamilyLink(familyLinkId: String): Result<Unit> {
        return request { careCircleId ->
            val response = api.revokeFamilyLink(careCircleId, familyLinkId)
            if (response.isSuccessful) Result.success(Unit) else Result.failure(response.toApiException())
        }
    }

    private suspend fun <T> request(block: suspend (careCircleId: String) -> Result<T>): Result<T> {
        try {
            val careCircleId = getCareCircleId().getOrElse { return Result.failure(it) }
            return block(careCircleId)

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    private suspend fun getCareCircleId(): Result<String> {
        val userId = sessionStorage.session.first()?.userId
            ?: return Result.failure(IllegalStateException("There is no active session"))

        cachedCareCircle?.let { (cachedUserId, careCircleId) ->
            if (cachedUserId == userId) return Result.success(careCircleId)
        }

        return api.getCareCircle(userId).toResult { careCircle ->
            cachedCareCircle = userId to careCircle.id
            careCircle.id
        }
    }

    private fun <D, T> Response<D>.toResult(transform: (D) -> T): Result<T> {
        if (isSuccessful) {
            body()?.let { dto ->
                return Result.success(transform(dto))
            }
        }
        return Result.failure(toApiException())
    }

    private companion object {
        const val PENDING = "PENDING"
        const val ACTIVE = "ACTIVE"
    }
}
