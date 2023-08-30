package com.ziss.storyapp.data.repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.ziss.storyapp.Injection.provideStoryDatabase
import com.ziss.storyapp.Injection.provideStoryRemoteDataSource
import com.ziss.storyapp.data.datasources.story.StoryRemoteDataSource
import com.ziss.storyapp.data.datasources.utils.StoryRemoteMediator
import com.ziss.storyapp.data.datasources.utils.db.StoryDatabase
import com.ziss.storyapp.data.models.StoryModel
import com.ziss.storyapp.utils.ResultState
import java.io.File

class StoryRepository private constructor(
    private val remoteDataSource: StoryRemoteDataSource,
    private val database: StoryDatabase
) {
    fun addStory(
        token: String, file: File, description: String, lat: Double?, lon: Double?
    ) = remoteDataSource.addStory(token, file, description, lat, lon)

    @OptIn(ExperimentalPagingApi::class)
    fun getStories(token: String) = liveData {
        emit(ResultState.Loading)

        try {
            val response = Pager(
                config = PagingConfig(
                    pageSize = 10
                ),
                remoteMediator = StoryRemoteMediator(token, remoteDataSource, database),
                pagingSourceFactory = {
                    database.storyDao().getStories()
                }
            ).liveData
            val result: LiveData<ResultState<PagingData<StoryModel>>> =
                response.map { ResultState.Success(it) }
            emitSource(result)
        } catch (e: Exception) {
            emit(ResultState.Failed(e.message.toString()))
        }
    }

    fun getStoriesWithLocation(token: String) = remoteDataSource.getStoriesWithLocation(token)

    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        @JvmStatic
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: StoryRepository(
                provideStoryRemoteDataSource(),
                provideStoryDatabase(context)
            )
        }.also { instance = it }
    }
}