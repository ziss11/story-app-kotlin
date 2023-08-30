package com.ziss.storyapp.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ziss.storyapp.Injection.provideAuthRepository
import com.ziss.storyapp.Injection.provideStoryRepository
import com.ziss.storyapp.data.repositories.AuthRepository
import com.ziss.storyapp.data.repositories.StoryRepository
import com.ziss.storyapp.dataStore
import com.ziss.storyapp.presentation.ui.activities.addStory.AddStoryViewModel
import com.ziss.storyapp.presentation.ui.fragments.home.HomeViewModel
import com.ziss.storyapp.presentation.ui.fragments.home.MapsViewModel
import com.ziss.storyapp.presentation.viewmodels.LoginViewModel
import com.ziss.storyapp.presentation.viewmodels.RegisterViewModel

class ViewModelFactory private constructor(
    private val authRepository: AuthRepository, private val storyRepository: StoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(authRepository) as T
        } else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(storyRepository) as T
        } else if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(storyRepository) as T
        }

        throw IllegalArgumentException("Unknown view model class: $modelClass")
    }

    companion object {
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    provideAuthRepository(context.dataStore),
                    provideStoryRepository(context)
                )
            }.also { instance = it }
    }
}