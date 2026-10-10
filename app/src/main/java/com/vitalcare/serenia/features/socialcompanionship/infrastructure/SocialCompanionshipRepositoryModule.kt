package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderRepository

@Module
@InstallIn(SingletonComponent::class)
interface SocialCompanionshipRepositoryModule {

    @Binds
    fun provideReminderRepository(impl: ReminderRepositoryImpl): ReminderRepository
}
