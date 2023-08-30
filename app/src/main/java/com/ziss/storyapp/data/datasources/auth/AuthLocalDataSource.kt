package com.ziss.storyapp.data.datasources.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.ziss.storyapp.Injection.provideAuthPreferences
import com.ziss.storyapp.data.datasources.utils.db.AuthPreferences

interface AuthLocalDataSource {
    fun getToken(): LiveData<String>
    suspend fun saveToken(token: String)
}

class AuthLocalDataSourceImpl private constructor(private val pref: AuthPreferences) :
    AuthLocalDataSource {
    override fun getToken() = pref.getToken().asLiveData()

    override suspend fun saveToken(token: String) = pref.saveToken(token)

    companion object {
        @Volatile
        private var instance: AuthLocalDataSource? = null

        @JvmStatic
        fun getInstance(dataStore: DataStore<Preferences>) = instance ?: synchronized(this) {
            instance ?: AuthLocalDataSourceImpl(provideAuthPreferences(dataStore))
        }.also { instance = it }
    }
}