package com.ziss.storyapp.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.util.Date

@Entity(tableName = "story")
@Parcelize
data class StoryModel(
    @PrimaryKey
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
