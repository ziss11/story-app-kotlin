package com.ziss.storyapp.data.datasources.story

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.ziss.storyapp.Injection.provideApiService
import com.ziss.storyapp.data.datasources.utils.service.ApiService
import com.ziss.storyapp.data.models.BaseResponse
import com.ziss.storyapp.data.models.StoriesResponse
import com.ziss.storyapp.data.models.StoryDetailResponse
import com.ziss.storyapp.utils.ResultState
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

interface StoryRemoteDataSource {
    fun addStory(
        token: String,
        file: File,
        description: String,
        lat: Double?,
        lon: Double?
    ): LiveData<ResultState<BaseResponse>>

    fun getStories(
        token: String,
        page: Int?,
        size: Int?
    ): LiveData<ResultState<StoriesResponse>>

    fun getStoryById(token: String, id: String): LiveData<ResultState<StoryDetailResponse>>
}

class StoryRemoteDataSourceImpl private constructor(private val apiService: ApiService) :
    StoryRemoteDataSource {
    private val addStoryResult = MediatorLiveData<ResultState<BaseResponse>>()
    private val storiesResult = MediatorLiveData<ResultState<StoriesResponse>>()
    private val storyDetailResult = MediatorLiveData<ResultState<StoryDetailResponse>>()

    override fun addStory(
        token: String,
        file: File,
        description: String,
        lat: Double?,
        lon: Double?
    ): LiveData<ResultState<BaseResponse>> {
        val bearerToken = "Bearer $token"
        val requestImageFile = file.asRequestBody("image/jpg".toMediaType())
        val imageMultipart = MultipartBody.Part.createFormData(
            "photo", file.name, requestImageFile
        )

        val descriptionBody = description.toRequestBody("text/plain".toMediaType())
        val latitudeBody = lat.toString().toRequestBody("text/plain".toMediaType())
        val longitudeBody = lon.toString().toRequestBody("text/plain".toMediaType())

        addStoryResult.value = ResultState.Loading

        val client = if (lat != null && lon != null) {
            apiService.addStory(
                bearerToken,
                imageMultipart,
                descriptionBody,
                latitudeBody,
                longitudeBody
            )
        } else {
            apiService.addStory(bearerToken, imageMultipart, descriptionBody)
        }

        client.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null) {
                    addStoryResult.value = ResultState.Success(responseBody)
                } else {
                    addStoryResult.value = ResultState.Failed(response.message())
                    Log.d(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                addStoryResult.value = ResultState.Failed(t.message.toString())
                Log.d(TAG, t.message.toString())
            }
        })

        return addStoryResult
    }

    override fun getStories(
        token: String,
        page: Int?,
        size: Int?
    ): LiveData<ResultState<StoriesResponse>> {
        storiesResult.value = ResultState.Loading

        val bearerToken = "Bearer $token"
        val client = apiService.getStories(bearerToken, page, size)
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null) {
                    storiesResult.value = ResultState.Success(responseBody)
                } else {
                    storiesResult.value = ResultState.Failed(response.message())
                    Log.d(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                storiesResult.value = ResultState.Failed(t.message.toString())
                Log.d(TAG, t.message.toString())
            }
        })

        return storiesResult
    }


    override fun getStoryById(
        token: String,
        id: String
    ): LiveData<ResultState<StoryDetailResponse>> {
        storyDetailResult.value = ResultState.Loading

        val bearerToken = "Bearer $token"
        val client = apiService.getStoryById(bearerToken, id)
        client.enqueue(object : Callback<StoryDetailResponse> {
            override fun onResponse(
                call: Call<StoryDetailResponse>,
                response: Response<StoryDetailResponse>
            ) {
                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null) {
                    storyDetailResult.value = ResultState.Success(responseBody)
                } else {
                    storyDetailResult.value = ResultState.Failed(response.message())
                    Log.d(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<StoryDetailResponse>, t: Throwable) {
                storyDetailResult.value = ResultState.Failed(t.message.toString())
                Log.d(TAG, t.message.toString())
            }
        })

        return storyDetailResult
    }

    companion object {
        private var TAG = StoryRemoteDataSource::class.java.simpleName
        private var instance: StoryRemoteDataSourceImpl? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: StoryRemoteDataSourceImpl(provideApiService())
        }.also { instance = it }
    }
}