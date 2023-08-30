package com.ziss.storyapp.presentation.ui.home

import androidx.lifecycle.ViewModel
import com.ziss.storyapp.data.repositories.StoryRepository

class HomeViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStories(token: String) = repository.getStories(token)
    fun getStoriesWithLocation(token: String) = repository.getStoriesWithLocation(token)
}