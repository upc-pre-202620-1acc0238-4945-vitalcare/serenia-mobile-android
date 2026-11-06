package com.vitalcare.serenia.features.iam.application

import com.vitalcare.serenia.features.iam.domain.AuthRepository
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(currentPassword: String, newPassword: String): Result<Unit> {
        return repository.changePassword(currentPassword, newPassword)
    }
}
