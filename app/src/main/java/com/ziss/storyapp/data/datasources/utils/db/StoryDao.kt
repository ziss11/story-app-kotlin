package com.ziss.storyapp.data.datasources.utils.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ziss.storyapp.data.models.StoryModel

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryModel>)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, StoryModel>

    @Query("DELETE FROM story")
    fun deleteStories()
}