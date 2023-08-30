package com.ziss.storyapp.presentation.ui.fragments.home

import androidx.lifecycle.ViewModel
import com.ziss.storyapp.data.repositories.StoryRepository

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStoriesWithLocation(token: String) = repository.getStoriesWithLocation(token)
}