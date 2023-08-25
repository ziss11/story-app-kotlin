package com.ziss.storyapp.data.datasources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.ziss.storyapp.Injection.provideAuthPreferences
import com.ziss.storyapp.data.datasources.preference.AuthPreferences

interface AuthLocalDataSource {
    fun getToken(): LiveData<String>
    suspend fun saveToken(token: String)
}

class AuthLocalDataSourceImpl private constructor(private val pref: AuthPreferences) :
    AuthLocalDataSource {
    override fun getToken(): LiveData<String> {
        return pref.getToken().asLiveData()
    }

    override suspend fun saveToken(token: String) {
        pref.saveToken(token)
    }

    companion object {
        private var instance: AuthLocalDataSource? = null

        fun getInstance(dataStore: DataStore<Preferences>) = instance ?: synchronized(this) {
            instance ?: AuthLocalDataSourceImpl(provideAuthPreferences(dataStore))
        }.also { instance = it }
    }
}