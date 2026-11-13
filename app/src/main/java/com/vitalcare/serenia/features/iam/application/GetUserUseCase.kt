package com.vitalcare.serenia.features.iam.application

import com.vitalcare.serenia.features.iam.domain.AuthRepository
import com.vitalcare.serenia.features.iam.domain.User
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(userId: String): Result<User> {
        return repository.getUser(userId)
    }
}
