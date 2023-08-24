package com.ziss.storyapp.data.models

import com.google.gson.annotations.SerializedName

data class DetailStoryResponse(
    @field: SerializedName("error")
    override val error: Boolean,

    @field: SerializedName("message")
    override val message: String,

    @field: SerializedName("listStory")
    val story: StoryModel
) : BaseResponse(error, message)