package com.vitalcare.serenia.features.iam.infrastructure

import android.os.Build
import com.vitalcare.serenia.core.network.toApiException
import com.vitalcare.serenia.core.session.Session
import com.vitalcare.serenia.core.session.SessionStorage
import com.vitalcare.serenia.features.iam.domain.AuthRepository
import com.vitalcare.serenia.features.iam.domain.User
import com.vitalcare.serenia.features.iam.domain.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.TimeZone
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: IamApi,
    private val sessionStorage: SessionStorage
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<User> {
        try {
            val request = SignInRequestDto(
                email = email,
                password = password,
                deviceInfo = "Android ${Build.VERSION.RELEASE} - ${Build.MANUFACTURER} ${Build.MODEL}"
            )
            val response = api.signIn(request)

            if (response.isSuccessful) {
                response.body()?.let { authenticatedUser ->
                    sessionStorage.save(
                        Session(
                            sessionId = authenticatedUser.sessionId,
                            token = authenticatedUser.token,
                            userId = authenticatedUser.user.id
                        )
                    )
                    return Result.success(authenticatedUser.user.toDomain())
                }
            }
            return Result.failure(response.toApiException())

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    override suspend fun register(
        fullName: String,
        email: String,
        password: String,
        role: UserRole
    ): Result<User> {
        try {
            val request = RegisterUserRequestDto(
                email = email,
                password = password,
                role = role.name,
                fullName = fullName,
                phoneNumber = null,
                birthDate = null,
                locale = LOCALE,
                timeZone = TimeZone.getDefault().id
            )
            val response = api.register(request)

            if (response.isSuccessful) {
                response.body()?.let { userDto ->
                    return Result.success(userDto.toDomain())
                }
            }
            return Result.failure(response.toApiException())

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        try {
            val userId = sessionStorage.session.first()?.userId
                ?: return Result.failure(IllegalStateException("There is no active session"))
            val response = api.getUser(userId)

            if (response.isSuccessful) {
                response.body()?.let { userDto ->
                    return Result.success(userDto.toDomain())
                }
            }
            return Result.failure(response.toApiException())

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    override fun isSignedIn(): Flow<Boolean> {
        return sessionStorage.session.map { session -> session != null }
    }

    private companion object {
        const val LOCALE = "es-PE"
    }
}
