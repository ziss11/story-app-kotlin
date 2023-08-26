package com.ziss.storyapp.data.models

import com.google.gson.annotations.SerializedName

data class StoryDetailResponse(
    @field: SerializedName("error")
    val error: Boolean,

    @field: SerializedName("message")
    val message: String,

    @field: SerializedName("story")
    val story: StoryModel
)