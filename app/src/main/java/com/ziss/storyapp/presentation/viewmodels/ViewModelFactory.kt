package com.ziss.storyapp.presentation.viewmodels

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ziss.storyapp.Injection.provideAuthRepository
import com.ziss.storyapp.data.repositories.AuthRepository

class ViewModelFactory private constructor(private val authRepository: AuthRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository) as T
        }

        throw IllegalArgumentException("Unknown view model class: $modelClass")
    }

    companion object {
        private var instance: ViewModelFactory? = null

        fun getInstance(dataStore: DataStore<Preferences>) =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(provideAuthRepository(dataStore))
            }.also { instance = it }
    }
}