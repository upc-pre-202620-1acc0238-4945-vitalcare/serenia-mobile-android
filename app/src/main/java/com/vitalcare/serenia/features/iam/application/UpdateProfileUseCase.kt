package com.vitalcare.serenia.features.iam.application

import com.vitalcare.serenia.features.iam.domain.AuthRepository
import com.vitalcare.serenia.features.iam.domain.BirthDate
import com.vitalcare.serenia.features.iam.domain.User
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(fullName: String, phoneNumber: String?, birthDate: BirthDate?): Result<User> {
        return repository.updateProfile(fullName.trim(), phoneNumber, birthDate)
    }
}
