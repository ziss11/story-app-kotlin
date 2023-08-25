package com.ziss.storyapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ziss.storyapp.data.repositories.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun login(email: String, password: String) = authRepository.login(email, password)
    fun register(name: String, email: String, password: String) =
        authRepository.register(name, email, password)

    fun getToken() = authRepository.getToken()

    fun setToken(token: String) {
        viewModelScope.launch {
            authRepository.setToken(token)
        }
    }
}