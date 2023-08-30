package com.ziss.storyapp.data.repositories

import android.content.Context
import com.ziss.storyapp.Injection.provideStoryRemoteDataSource
import com.ziss.storyapp.data.datasources.story.StoryRemoteDataSource
import java.io.File

class StoryRepository private constructor(private val remoteDataSource: StoryRemoteDataSource) {
    fun addStory(
        file: File, description: String, lat: Double?, lon: Double?
    ) = remoteDataSource.addStory(file, description, lat, lon)

    fun getStories(token: String) = remoteDataSource.getStories(token)

    companion object {
        private var instance: StoryRepository? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: StoryRepository(provideStoryRemoteDataSource(context))
        }.also { instance = it }
    }
}