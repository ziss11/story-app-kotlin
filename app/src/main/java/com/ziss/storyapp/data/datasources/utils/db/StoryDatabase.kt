package com.ziss.storyapp.data.datasources.utils.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ziss.storyapp.data.models.RemoteKeys
import com.ziss.storyapp.data.models.StoryModel
import com.ziss.storyapp.utils.Converter

@Database(
    entities = [StoryModel::class, RemoteKeys::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null

        @JvmStatic
        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                StoryDatabase::class.java,
                "story_db",
            ).fallbackToDestructiveMigration()
                .build()
                .also { instance = it }
        }
    }
}