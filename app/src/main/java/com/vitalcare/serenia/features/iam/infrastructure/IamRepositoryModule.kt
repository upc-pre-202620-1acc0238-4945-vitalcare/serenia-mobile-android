package com.vitalcare.serenia.features.iam.infrastructure

import com.vitalcare.serenia.features.iam.domain.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface IamRepositoryModule {

    @Binds
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
