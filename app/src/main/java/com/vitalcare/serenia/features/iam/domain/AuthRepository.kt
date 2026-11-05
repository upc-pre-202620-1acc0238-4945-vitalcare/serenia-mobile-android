package com.vitalcare.serenia.features.iam.domain

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signIn(email: String, password: String): Result<User>

    suspend fun register(fullName: String, email: String, password: String, role: UserRole): Result<User>

    fun isSignedIn(): Flow<Boolean>
}
