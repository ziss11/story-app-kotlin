package com.ziss.storyapp.data.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class StoryModel(
    @field: SerializedName("id")
    val id: String,

    @field: SerializedName("name")
    val name: String,

    @field: SerializedName("description")
    val description: String,

    @field: SerializedName("photoUrl")
    val photoUrl: String,

    @field: SerializedName("createdAt")
    val createdAt: Date,

    @field: SerializedName("lat")
    val lat: Double,

    @field: SerializedName("lon")
    val lon: Double,
)
