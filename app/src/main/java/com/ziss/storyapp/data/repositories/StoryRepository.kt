package com.ziss.storyapp.data.repositories

import com.ziss.storyapp.Injection.provideStoryRemoteDataSource
import com.ziss.storyapp.data.datasources.story.StoryRemoteDataSource
import java.io.File

class StoryRepository private constructor(private val remoteDataSource: StoryRemoteDataSource) {
    fun addStory(
        token: String, file: File, description: String, lat: Double?, lon: Double?
    ) = remoteDataSource.addStory(token, file, description, lat, lon)

    fun getStories(token: String, page: Int?, size: Int?) =
        remoteDataSource.getStories(token, page, size)

    fun getStoryById(token: String, id: String) = remoteDataSource.getStoryById(token, id)

    companion object {
        private var TAG = StoryRepository::class.java
        private var instance: StoryRepository? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: StoryRepository(provideStoryRemoteDataSource())
        }.also { instance = it }
    }
}