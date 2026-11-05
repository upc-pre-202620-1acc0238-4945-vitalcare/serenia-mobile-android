package com.vitalcare.serenia.features.iam.application

import com.vitalcare.serenia.features.iam.domain.AuthRepository
import com.vitalcare.serenia.features.iam.domain.User
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.signIn(email.trim(), password)
    }
}
