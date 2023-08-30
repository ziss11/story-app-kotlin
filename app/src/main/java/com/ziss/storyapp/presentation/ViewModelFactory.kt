package com.ziss.storyapp.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ziss.storyapp.Injection.provideAuthRepository
import com.ziss.storyapp.Injection.provideStoryRepository
import com.ziss.storyapp.data.repositories.AuthRepository
import com.ziss.storyapp.data.repositories.StoryRepository
import com.ziss.storyapp.presentation.ui.addStory.AddStoryViewModel
import com.ziss.storyapp.presentation.ui.home.HomeViewModel
import com.ziss.storyapp.presentation.viewmodels.LoginViewModel
import com.ziss.storyapp.presentation.viewmodels.RegisterViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository, private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(authRepository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(storyRepository) as T
            }

            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown view model class: $modelClass")
        }
    }

    companion object {
        private var instance: ViewModelFactory? = null

        fun getInstance(dataStore: DataStore<Preferences>) =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    provideAuthRepository(dataStore),
                    provideStoryRepository()
                )
            }.also { instance = it }
    }
}