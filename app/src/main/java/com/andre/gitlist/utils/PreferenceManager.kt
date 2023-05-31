package com.andre.gitlist.utils

import android.app.UiModeManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class PreferenceManager private constructor(context: Context) {
    companion object {
        private lateinit var dataStore: DataStore<Preferences>
        private val IS_DARK_MODE_KEY = intPreferencesKey("is_dark_mode")
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

        fun getInstance(context: Context): PreferenceManager {
            if (!::dataStore.isInitialized) {
                dataStore = context.dataStore
            }
            return PreferenceManager(context)
        }
    }

    val uiMode: Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[IS_DARK_MODE_KEY] ?: UiModeManager.MODE_NIGHT_NO
        }

    suspend fun setUiMode(mode: Int) {
        dataStore.edit { settings ->
            settings[IS_DARK_MODE_KEY] = mode
        }
    }

}