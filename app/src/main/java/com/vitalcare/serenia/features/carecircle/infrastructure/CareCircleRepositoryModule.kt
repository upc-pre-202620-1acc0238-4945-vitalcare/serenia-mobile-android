package com.vitalcare.serenia.features.carecircle.infrastructure

import com.vitalcare.serenia.features.carecircle.domain.CareCircleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CareCircleRepositoryModule {

    @Binds
    fun provideCareCircleRepository(impl: CareCircleRepositoryImpl): CareCircleRepository
}
