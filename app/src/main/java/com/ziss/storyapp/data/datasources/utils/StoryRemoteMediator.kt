package com.ziss.storyapp.data.datasources.utils

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ziss.storyapp.data.datasources.story.StoryRemoteDataSource
import com.ziss.storyapp.data.datasources.utils.db.StoryDatabase
import com.ziss.storyapp.data.models.StoryModel

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    val token: String,
    private val remoteDataSource: StoryRemoteDataSource,
    private val database: StoryDatabase
) :
    RemoteMediator<Int, StoryModel>() {
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryModel>
    ): MediatorResult {
        val page = INITIAL_PAGE_INDEX

        return try {
            val bearerToken = "Bearer $token"
            val responseData =
                remoteDataSource.getStories(bearerToken, page, state.config.pageSize).stories
            Log.d("Story", responseData.toString())
            val endOfPaginationReached = responseData.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteStories()
                }
                database.storyDao().insertStories(responseData)
            }
            MediatorResult.Success(endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}