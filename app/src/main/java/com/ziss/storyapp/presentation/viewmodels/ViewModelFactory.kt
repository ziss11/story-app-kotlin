package com.ziss.storyapp.presentation.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ziss.storyapp.Injection.provideAuthRepository
import com.ziss.storyapp.Injection.provideStoryRepository
import com.ziss.storyapp.data.repositories.AuthRepository
import com.ziss.storyapp.data.repositories.StoryRepository

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository, private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                StoryViewModel(storyRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown view model class: $modelClass")
        }
    }

    companion object {
        private var instance: ViewModelFactory? = null

        fun getInstance(dataStore: DataStore<Preferences>) = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(
                provideAuthRepository(dataStore), provideStoryRepository()
            )
        }.also { instance = it }
    }
}