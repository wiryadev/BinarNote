package com.wiryadev.binarnote.data.preference

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import javax.inject.Inject

class AuthPreference @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
}