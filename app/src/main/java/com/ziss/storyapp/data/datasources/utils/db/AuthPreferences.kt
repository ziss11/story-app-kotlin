package com.ziss.storyapp.data.datasources.utils.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.map

class AuthPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    fun getToken() = dataStore.data.map { preference ->
        preference[tokenKey] ?: ""
    }


    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[tokenKey] = token
        }
    }

    companion object {
        private val tokenKey = stringPreferencesKey("token")
        private var instance: AuthPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>) = instance ?: synchronized(this) {
            instance ?: AuthPreferences(dataStore)
        }.also { instance = it }
    }
}