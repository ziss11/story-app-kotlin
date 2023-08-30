package com.ziss.storyapp.presentation.ui.addStory

import androidx.lifecycle.ViewModel
import com.ziss.storyapp.data.repositories.StoryRepository
import java.io.File

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun addStory(
        token: String,
        file: File,
        description: String,
        lat: Double? = null,
        lon: Double? = null
    ) =
        repository.addStory(token, file, description, lat, lon)
}