package com.ziss.storyapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.ziss.storyapp.data.repositories.StoryRepository
import java.io.File

class StoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun addStory(token: String, file: File, description: String, lat: Double, lon: Double) =
        repository.addStory(token, file, description, lat, lon)

    fun getStories(token: String, page: Int? = 1, size: Int? = 10) =
        repository.getStories(token, page, size)

    fun getStoryById(token: String, id: String) = repository.getStoryById(token, id)
}