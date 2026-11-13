package com.vitalcare.serenia.features.iam.application

import com.vitalcare.serenia.features.iam.domain.AuthRepository
import com.vitalcare.serenia.features.iam.domain.ProfilePhotoReader
import com.vitalcare.serenia.features.iam.domain.User
import javax.inject.Inject

class UploadProfilePhotoUseCase @Inject constructor(
    private val photoReader: ProfilePhotoReader,
    private val repository: AuthRepository
) {

    suspend operator fun invoke(photoUri: String): Result<User> {
        val photo = photoReader.read(photoUri).getOrElse { return Result.failure(it) }
        return repository.uploadProfilePhoto(photo)
    }
}
