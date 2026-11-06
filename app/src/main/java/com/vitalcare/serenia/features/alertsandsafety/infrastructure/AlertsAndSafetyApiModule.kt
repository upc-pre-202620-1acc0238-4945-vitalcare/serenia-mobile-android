package com.vitalcare.serenia.features.alertsandsafety.infrastructure

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlertsAndSafetyApiModule {

    @Provides
    @Singleton
    fun provideAlertsAndSafetyApi(retrofit: Retrofit): AlertsAndSafetyApi {
        return retrofit.create(AlertsAndSafetyApi::class.java)
    }
}
