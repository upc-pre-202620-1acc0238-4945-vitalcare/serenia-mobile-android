package com.vitalcare.serenia.features.iam.application

import com.vitalcare.serenia.features.iam.domain.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(): Result<Unit> {
        return repository.signOut()
    }
}
