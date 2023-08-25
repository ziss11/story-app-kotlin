package com.ziss.storyapp.utils

sealed class ResultState<out R> private constructor() {
    object Loading : ResultState<Nothing>()
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Failed(val message: String) : ResultState<Nothing>()
}
