package com.ziss.storyapp.data.datasources.service

import com.ziss.storyapp.data.models.BaseResponse
import com.ziss.storyapp.data.models.StoriesResponse
import com.ziss.storyapp.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST(Constants.REGISTER_PATH)
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<BaseResponse>

    @FormUrlEncoded
    @POST(Constants.LOGIN_PATH)
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<String>

    @Multipart
    @POST(Constants.STORIES_PATH)
    fun addStory(
        @Header("Authorization") token: String,
        @Part("photo") photo: MultipartBody.Part,
        @Part("description") descriptions: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,
    ): Call<BaseResponse>

    @GET(Constants.STORIES_PATH)
    fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
    ): Call<StoriesResponse>

    @GET(Constants.STORIES_PATH + "/{id}")
    fun getStoryById(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Call<StoriesResponse>
}