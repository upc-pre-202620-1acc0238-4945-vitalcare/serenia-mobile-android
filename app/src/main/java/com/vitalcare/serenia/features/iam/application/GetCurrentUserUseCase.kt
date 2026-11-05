package com.vitalcare.serenia.features.iam.application

import com.vitalcare.serenia.features.iam.domain.AuthRepository
import com.vitalcare.serenia.features.iam.domain.User
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(): Result<User> {
        return repository.getCurrentUser()
    }
}
