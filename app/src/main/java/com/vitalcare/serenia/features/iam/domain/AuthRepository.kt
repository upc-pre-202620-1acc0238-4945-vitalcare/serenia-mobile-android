package com.vitalcare.serenia.features.iam.domain

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signIn(email: String, password: String): Result<User>

    suspend fun register(
        fullName: String,
        email: String,
        password: String,
        role: UserRole,
        phoneNumber: String?,
        birthDate: BirthDate?
    ): Result<User>

    suspend fun getCurrentUser(): Result<User>

    suspend fun getUser(userId: String): Result<User>

    suspend fun updateProfile(fullName: String, phoneNumber: String?, birthDate: BirthDate?): Result<User>

    suspend fun uploadProfilePhoto(photo: ByteArray): Result<User>

    suspend fun changePassword(currentPassword: String, newPassword: String): Result<Unit>

    suspend fun signOut(): Result<Unit>

    fun isSignedIn(): Flow<Boolean>
}
