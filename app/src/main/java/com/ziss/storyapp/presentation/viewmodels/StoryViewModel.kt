package com.ziss.storyapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.ziss.storyapp.data.repositories.StoryRepository
import java.io.File

class StoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun addStory(
        file: File,
        description: String,
        lat: Double? = null,
        lon: Double? = null
    ) =
        repository.addStory(file, description, lat, lon)

    fun getStories(token: String) = repository.getStories(token)
}