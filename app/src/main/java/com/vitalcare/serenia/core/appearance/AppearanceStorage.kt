package com.vitalcare.serenia.core.appearance

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Preferencias de apariencia guardadas solo en el dispositivo.
 *
 * Usa su propio archivo (no el de la sesión) para que el alto contraste
 * siga activo después de cerrar sesión y también se vea en el inicio de sesión.
 */
@Singleton
class AppearanceStorage @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile("appearance")
    }

    val highContrast: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[HIGH_CONTRAST] ?: false }
        .distinctUntilChanged()

    suspend fun setHighContrast(enabled: Boolean) {
        dataStore.edit { preferences -> preferences[HIGH_CONTRAST] = enabled }
    }

    private companion object {
        val HIGH_CONTRAST = booleanPreferencesKey("high_contrast")
    }
}
