package com.ziss.storyapp.data.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field: SerializedName("error")
    override val error: Boolean,

    @field: SerializedName("message")
    override val message: String,

    @field: SerializedName("loginResult")
    val loginResult: LoginResult
) : BaseResponse(error, message)