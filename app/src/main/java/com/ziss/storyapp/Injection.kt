package com.ziss.storyapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.ziss.storyapp.data.datasources.auth.AuthLocalDataSourceImpl
import com.ziss.storyapp.data.datasources.auth.AuthRemoteDataSourceImpl
import com.ziss.storyapp.data.datasources.story.StoryRemoteDataSourceImpl
import com.ziss.storyapp.data.datasources.utils.preference.AuthPreferences
import com.ziss.storyapp.data.datasources.utils.service.ApiConfig
import com.ziss.storyapp.data.datasources.utils.service.ApiService
import com.ziss.storyapp.data.repositories.AuthRepository
import com.ziss.storyapp.data.repositories.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideApiService(context: Context): ApiService {
        val pref = AuthPreferences.getInstance(context.dataStore)
        val token = runBlocking { pref.getToken().first() }
        return ApiConfig.getApiService(token)
    }

    fun provideAuthPreferences(dataStore: DataStore<Preferences>) =
        AuthPreferences.getInstance(dataStore)

    fun provideAuthRemoteDataSource(context: Context) =
        AuthRemoteDataSourceImpl.getInstance(context)

    fun provideAuthLocalDataSource(dataStore: DataStore<Preferences>) =
        AuthLocalDataSourceImpl.getInstance(dataStore)

    fun provideStoryRemoteDataSource(context: Context) =
        StoryRemoteDataSourceImpl.getInstance(context)

    fun provideAuthRepository(context: Context) = AuthRepository.getInstance(context)

    fun provideStoryRepository(context: Context) = StoryRepository.getInstance(context)
}