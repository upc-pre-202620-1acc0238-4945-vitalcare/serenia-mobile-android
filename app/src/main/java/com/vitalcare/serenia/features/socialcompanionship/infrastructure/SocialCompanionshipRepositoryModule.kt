package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import com.vitalcare.serenia.features.socialcompanionship.domain.AudioMessageRepository
import com.vitalcare.serenia.features.socialcompanionship.domain.PhotoMessageRepository
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminderRepository
import com.vitalcare.serenia.features.socialcompanionship.domain.VoicePlayer
import com.vitalcare.serenia.features.socialcompanionship.domain.VoiceRecorder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface SocialCompanionshipRepositoryModule {

    @Binds
    fun provideSocialReminderRepository(impl: SocialReminderRepositoryImpl): SocialReminderRepository

    @Binds
    fun provideAudioMessageRepository(impl: AudioMessageRepositoryImpl): AudioMessageRepository

    @Binds
    fun providePhotoMessageRepository(impl: PhotoMessageRepositoryImpl): PhotoMessageRepository

    @Binds
    fun provideVoiceRecorder(impl: MediaVoiceRecorder): VoiceRecorder

    @Binds
    fun provideVoicePlayer(impl: MediaVoicePlayer): VoicePlayer
}
