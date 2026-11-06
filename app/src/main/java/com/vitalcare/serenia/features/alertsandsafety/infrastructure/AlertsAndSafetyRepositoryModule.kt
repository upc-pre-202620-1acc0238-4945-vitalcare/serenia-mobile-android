package com.vitalcare.serenia.features.alertsandsafety.infrastructure

import com.vitalcare.serenia.features.alertsandsafety.domain.EmergencyAlertRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface AlertsAndSafetyRepositoryModule {

    @Binds
    fun provideEmergencyAlertRepository(impl: EmergencyAlertRepositoryImpl): EmergencyAlertRepository
}
