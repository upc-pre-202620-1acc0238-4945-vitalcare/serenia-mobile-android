package com.vitalcare.serenia.features.socialcompanionship.infrastructure

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SocialCompanionshipApiModule {

    @Provides
    @Singleton
    fun provideSocialCompanionshipApi(retrofit: Retrofit): SocialCompanionshipApi {
        return retrofit.create(SocialCompanionshipApi::class.java)
    }
}
