package com.wiryadev.binarnote.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthPreference @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    fun getUserSession(): Flow<AuthModel> = dataStore.data.map { pref ->
        AuthModel(
            username = pref[USERNAME_KEY] ?: "",
            email = pref[EMAIL_KEY] ?: "",
        )
    }

    suspend fun saveUserSession(user: AuthModel) {
        dataStore.edit { pref ->
            pref[USERNAME_KEY] = user.username
            pref[EMAIL_KEY] = user.email
        }
    }

    suspend fun deleteUserSession() {
        dataStore.edit { pref ->
            pref[USERNAME_KEY] = ""
            pref[EMAIL_KEY] = ""
        }
    }

}

private val USERNAME_KEY = stringPreferencesKey("username")
private val EMAIL_KEY = stringPreferencesKey("email")