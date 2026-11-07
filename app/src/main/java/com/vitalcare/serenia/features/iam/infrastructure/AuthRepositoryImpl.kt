package com.vitalcare.serenia.features.iam.infrastructure

import android.os.Build
import com.vitalcare.serenia.core.network.toApiException
import com.vitalcare.serenia.core.session.Session
import com.vitalcare.serenia.core.session.SessionStorage
import com.vitalcare.serenia.features.iam.domain.AuthRepository
import com.vitalcare.serenia.features.iam.domain.BirthDate
import com.vitalcare.serenia.features.iam.domain.IncorrectPasswordException
import com.vitalcare.serenia.features.iam.domain.User
import com.vitalcare.serenia.features.iam.domain.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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
        role: UserRole,
        phoneNumber: String?,
        birthDate: BirthDate?
    ): Result<User> {
        try {
            val request = RegisterUserRequestDto(
                email = email,
                password = password,
                role = role.name,
                fullName = fullName,
                phoneNumber = phoneNumber,
                birthDate = birthDate?.toRequestValue(),
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
            return getUser(userId)

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    override suspend fun getUser(userId: String): Result<User> {
        try {
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

    override suspend fun updateProfile(
        fullName: String,
        phoneNumber: String?,
        birthDate: BirthDate?
    ): Result<User> {
        try {
            val userId = sessionStorage.session.first()?.userId
                ?: return Result.failure(IllegalStateException("There is no active session"))
            val request = UpdateProfileRequestDto(
                fullName = fullName,
                phoneNumber = phoneNumber,
                birthDate = birthDate?.toRequestValue(),
                locale = LOCALE,
                timeZone = TimeZone.getDefault().id
            )
            val response = api.updateProfile(userId, request)

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

    override suspend fun uploadProfilePhoto(photo: ByteArray): Result<User> {
        try {
            val userId = sessionStorage.session.first()?.userId
                ?: return Result.failure(IllegalStateException("There is no active session"))
            val filePart = MultipartBody.Part.createFormData(
                PHOTO_PART,
                PHOTO_FILE_NAME,
                photo.toRequestBody(PHOTO_TYPE.toMediaType())
            )
            val response = api.uploadPhoto(userId, filePart)

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

    override suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit> {
        try {
            val userId = sessionStorage.session.first()?.userId
                ?: return Result.failure(IllegalStateException("There is no active session"))
            val response = api.changePassword(userId, ChangePasswordRequestDto(currentPassword, newPassword))

            if (response.isSuccessful) {
                return Result.success(Unit)
            }
            if (response.code() == INCORRECT_PASSWORD) {
                return Result.failure(IncorrectPasswordException())
            }
            return Result.failure(response.toApiException())

        } catch (exception: Exception) {
            return Result.failure(exception)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        try {
            val sessionId = sessionStorage.session.first()?.sessionId
                ?: return Result.success(Unit)
            val response = api.signOut(sessionId)

            if (response.isSuccessful || response.code() in SESSION_ALREADY_CLOSED) {
                sessionStorage.clear()
                return Result.success(Unit)
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
        const val INCORRECT_PASSWORD = 422
        const val PHOTO_PART = "file"
        const val PHOTO_FILE_NAME = "profile.jpg"
        const val PHOTO_TYPE = "image/jpeg"
        val SESSION_ALREADY_CLOSED = setOf(401, 404)
    }
}
