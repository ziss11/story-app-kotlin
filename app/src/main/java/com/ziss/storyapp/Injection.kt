package com.ziss.storyapp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ziss.storyapp.data.datasources.AuthLocalDataSourceImpl
import com.ziss.storyapp.data.datasources.AuthRemoteDataSourceImpl
import com.ziss.storyapp.data.datasources.preference.AuthPreferences
import com.ziss.storyapp.data.datasources.service.ApiConfig
import com.ziss.storyapp.data.repositories.AuthRepository

object Injection {
    fun provideApiService() = ApiConfig.getApiService()

    fun provideAuthPreferences(dataStore: DataStore<Preferences>) =
        AuthPreferences.getInstance(dataStore)

    fun provideAuthRemoteDataSource() = AuthRemoteDataSourceImpl.getInstance()

    fun provideAuthLocalDataSource(dataStore: DataStore<Preferences>) =
        AuthLocalDataSourceImpl.getInstance(dataStore)

    fun provideAuthRepository(dataStore: DataStore<Preferences>) =
        AuthRepository.getInstance(dataStore)
}