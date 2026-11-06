package com.vitalcare.serenia.features.carecircle.infrastructure

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CareCircleApiModule {

    @Provides
    @Singleton
    fun provideCareCircleApi(retrofit: Retrofit): CareCircleApi {
        return retrofit.create(CareCircleApi::class.java)
    }
}
