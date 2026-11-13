package com.vitalcare.serenia.features.iam.application

import com.vitalcare.serenia.features.iam.domain.AuthRepository
import com.vitalcare.serenia.features.iam.domain.BirthDate
import com.vitalcare.serenia.features.iam.domain.User
import com.vitalcare.serenia.features.iam.domain.UserRole
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(
        fullName: String,
        email: String,
        password: String,
        role: UserRole,
        phoneNumber: String?,
        birthDate: BirthDate?
    ): Result<User> {
        val trimmedEmail = email.trim()
        return repository.register(fullName.trim(), trimmedEmail, password, role, phoneNumber, birthDate)
            .mapCatching { repository.signIn(trimmedEmail, password).getOrThrow() }
    }
}
