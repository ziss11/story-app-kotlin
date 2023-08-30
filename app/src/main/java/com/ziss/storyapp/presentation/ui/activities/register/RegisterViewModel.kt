package com.ziss.storyapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.ziss.storyapp.data.repositories.AuthRepository

class RegisterViewModel(private val repository: AuthRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.register(name, email, password)
}