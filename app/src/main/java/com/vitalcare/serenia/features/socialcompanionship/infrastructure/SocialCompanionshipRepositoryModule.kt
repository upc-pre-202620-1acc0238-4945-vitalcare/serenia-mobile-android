package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SocialCompanionshipRepositoryModule {

    @Binds
    fun provideSocialReminderRepository(impl: SocialReminderRepositoryImpl): SocialReminderRepository
}
