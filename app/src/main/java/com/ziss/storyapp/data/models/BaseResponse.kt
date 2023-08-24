package com.ziss.storyapp.data.models

import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @field: SerializedName("error")
    open val error: Boolean,

    @field: SerializedName("message")
    open val message: String,
)
