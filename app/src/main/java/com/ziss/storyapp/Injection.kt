package com.ziss.storyapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ziss.storyapp.data.datasources.auth.AuthLocalDataSourceImpl
import com.ziss.storyapp.data.datasources.auth.AuthRemoteDataSource
import com.ziss.storyapp.data.datasources.story.StoryRemoteDataSourceImpl
import com.ziss.storyapp.data.datasources.utils.db.AuthPreferences
import com.ziss.storyapp.data.datasources.utils.db.StoryDatabase
import com.ziss.storyapp.data.datasources.utils.service.ApiConfig
import com.ziss.storyapp.data.repositories.AuthRepository
import com.ziss.storyapp.data.repositories.StoryRepository

object Injection {
    fun provideApiService() = ApiConfig.getApiService()

    fun provideAuthPreferences(dataStore: DataStore<Preferences>) =
        AuthPreferences.getInstance(dataStore)

    fun provideStoryDatabase(context: Context) = StoryDatabase.getInstance(context)

    fun provideAuthRemoteDataSource() = AuthRemoteDataSource.getInstance(ApiConfig.getApiService())

    fun provideAuthLocalDataSource(dataStore: DataStore<Preferences>) =
        AuthLocalDataSourceImpl.getInstance(dataStore)

    fun provideStoryRemoteDataSource() =
        StoryRemoteDataSourceImpl.getInstance(ApiConfig.getApiService())

    fun provideAuthRepository(dataStore: DataStore<Preferences>) =
        AuthRepository.getInstance(dataStore)

    fun provideStoryRepository(context: Context) = StoryRepository.getInstance(context)
}