package com.ziss.storyapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziss.storyapp.data.repositories.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    fun login(email: String, password: String) = repository.login(email, password)

    fun getToken() = repository.getToken()

    fun setToken(token: String) {
        viewModelScope.launch {
            repository.setToken(token)
        }
    }
}