package com.ziss.storyapp.data.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ziss.storyapp.Injection.provideAuthLocalDataSource
import com.ziss.storyapp.Injection.provideAuthRemoteDataSource
import com.ziss.storyapp.data.datasources.auth.AuthLocalDataSource
import com.ziss.storyapp.data.datasources.auth.AuthRemoteDataSource

class AuthRepository private constructor(
    private val remoteDataSource: AuthRemoteDataSource,
    private val localDataSource: AuthLocalDataSource
) {
    fun login(email: String, password: String) =
        remoteDataSource.login(email, password)

    fun register(name: String, email: String, password: String) =
        remoteDataSource.register(name, email, password)

    fun getToken() = localDataSource.getToken()

    suspend fun setToken(token: String) = localDataSource.saveToken(token)

    companion object {
        private var instance: AuthRepository? = null

        fun getInstance(dataStore: DataStore<Preferences>) =
            instance ?: synchronized(this) {
                instance ?: AuthRepository(
                    provideAuthRemoteDataSource(),
                    provideAuthLocalDataSource(dataStore)
                )
            }.also { instance = it }
    }
}