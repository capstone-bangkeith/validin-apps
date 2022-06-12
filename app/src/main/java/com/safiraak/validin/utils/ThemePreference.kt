package com.safiraak.validin.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreference private constructor(private val dataStore: DataStore<Preferences>) {
    private val THEME_KEY = booleanPreferencesKey("theme_set")

    fun getThemeSet() : Flow<Boolean>{
        return dataStore.data.map { pref ->
            pref[THEME_KEY] ?: false
        }
    }

    suspend fun saveThemeSet(isDarkMode: Boolean) {
        dataStore.edit { pref ->
            pref[THEME_KEY] = isDarkMode
        }
    }

    companion object {
        private var INSTANCE: ThemePreference? = null

        fun getInstance(dataStore: DataStore<Preferences>) : ThemePreference {
            return INSTANCE ?: synchronized(this) {
                val instance = ThemePreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}