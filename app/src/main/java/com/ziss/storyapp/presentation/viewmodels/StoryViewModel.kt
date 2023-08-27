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

    fun getStories(page: Int? = 1, size: Int? = 10) =
        repository.getStories(page, size)
}