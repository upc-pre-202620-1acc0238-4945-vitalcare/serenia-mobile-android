package com.vitalcare.serenia.features.iam.application

import com.vitalcare.serenia.features.iam.domain.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsSignedInUseCase @Inject constructor(private val repository: AuthRepository) {

    operator fun invoke(): Flow<Boolean> {
        return repository.isSignedIn()
    }
}
