package com.vitalcare.serenia.features.dailycheckin.infrastructure

import com.vitalcare.serenia.features.dailycheckin.domain.CheckInPreferencesRepository
import com.vitalcare.serenia.features.dailycheckin.domain.CheckInRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DailyCheckInRepositoryModule {

    @Binds
    fun provideCheckInRepository(impl: CheckInRepositoryImpl): CheckInRepository

    @Binds
    fun provideCheckInPreferencesRepository(impl: CheckInPreferencesRepositoryImpl): CheckInPreferencesRepository
}
