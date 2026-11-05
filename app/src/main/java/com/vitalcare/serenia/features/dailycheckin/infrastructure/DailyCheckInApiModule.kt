package com.vitalcare.serenia.features.dailycheckin.infrastructure

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DailyCheckInApiModule {

    @Provides
    @Singleton
    fun provideDailyCheckInApi(retrofit: Retrofit): DailyCheckInApi {
        return retrofit.create(DailyCheckInApi::class.java)
    }
}
