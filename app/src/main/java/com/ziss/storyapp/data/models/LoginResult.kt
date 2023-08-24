package com.ziss.storyapp.data.models

import com.google.gson.annotations.SerializedName

data class LoginResult(
    @field: SerializedName("token")
    val token: String
)
