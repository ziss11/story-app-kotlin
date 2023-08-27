package com.ziss.storyapp.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
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
) : Parcelable
