package com.vitalcare.serenia.features.iam.infrastructure

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IamApiModule {

    @Provides
    @Singleton
    fun provideIamApi(retrofit: Retrofit): IamApi {
        return retrofit.create(IamApi::class.java)
    }
}
