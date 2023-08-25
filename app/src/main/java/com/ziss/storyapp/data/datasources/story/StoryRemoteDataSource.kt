package com.ziss.storyapp.data.datasources.story

import androidx.lifecycle.LiveData
import com.ziss.storyapp.Injection.provideApiService
import com.ziss.storyapp.data.datasources.utils.service.ApiService
import com.ziss.storyapp.data.models.BaseResponse
import com.ziss.storyapp.data.models.StoriesResponse
import com.ziss.storyapp.data.models.StoryModel
import com.ziss.storyapp.utils.ResultState
import java.io.File

interface StoryRemoteDataSource {
    fun addStory(
        token: String,
        file: File,
        description: String,
        lat: Double?,
        lon: Double?
    ): LiveData<ResultState<BaseResponse>>

    fun getStories(token: String, page: Int?, int: Int?): LiveData<ResultState<StoriesResponse>>
    fun getStoryById(token: String, id: String): LiveData<ResultState<StoryModel>>
}

class StoryRemoteDataSourceImpl private constructor(apiService: ApiService) :
    StoryRemoteDataSource {
    override fun addStory(
        token: String,
        file: File,
        description: String,
        lat: Double?,
        lon: Double?
    ): LiveData<ResultState<BaseResponse>> {
        TODO("Not yet implemented")
    }

    override fun getStories(
        token: String,
        page: Int?,
        int: Int?
    ): LiveData<ResultState<StoriesResponse>> {
        TODO("Not yet implemented")
    }

    override fun getStoryById(token: String, id: String): LiveData<ResultState<StoryModel>> {
        TODO("Not yet implemented")
    }

    companion object {
        private var TAG = StoryRemoteDataSource::class.java.simpleName
        private var instance: StoryRemoteDataSourceImpl? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: StoryRemoteDataSourceImpl(provideApiService())
        }.also { instance = it }
    }
}