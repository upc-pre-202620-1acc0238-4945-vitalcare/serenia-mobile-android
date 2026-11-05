package com.vitalcare.serenia.core.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionStorage @Inject constructor(private val dataStore: DataStore<Preferences>) {

    val session: Flow<Session?> = dataStore.data.map { preferences ->
        val sessionId = preferences[SESSION_ID] ?: return@map null
        val token = preferences[TOKEN] ?: return@map null
        val userId = preferences[USER_ID] ?: return@map null
        Session(sessionId = sessionId, token = token, userId = userId)
    }

    suspend fun save(session: Session) {
        dataStore.edit { preferences ->
            preferences[SESSION_ID] = session.sessionId
            preferences[TOKEN] = session.token
            preferences[USER_ID] = session.userId
        }
    }

    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private companion object {
        val SESSION_ID = stringPreferencesKey("session_id")
        val TOKEN = stringPreferencesKey("token")
        val USER_ID = stringPreferencesKey("user_id")
    }
}
