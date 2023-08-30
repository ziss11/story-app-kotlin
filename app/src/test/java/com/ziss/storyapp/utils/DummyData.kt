package com.ziss.storyapp.utils

import com.ziss.storyapp.data.models.StoryModel
import java.util.Date

object DummyData {
    fun generateDummyStories(): List<StoryModel> {
        val items = arrayListOf<StoryModel>()

        for (i in 1..10) {
            val story = StoryModel(
                id = "ID_$i",
                name = "Story $i",
                description = "Description for Story $i",
                photoUrl = "https://example.com/photo_$i.jpg",
                createdAt = Date(),
                lat = 12.345 + i,
                lon = 67.890 - i,
            )
            items.add(story)
        }
        return items
    }
}