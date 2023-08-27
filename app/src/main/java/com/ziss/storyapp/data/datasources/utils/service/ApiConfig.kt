package com.ziss.storyapp.data.datasources.utils.service

import com.ziss.storyapp.utils.Constants
import com.ziss.storyapp.utils.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun getApiService(token: String? = ""): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val tokenInterceptor = if (!token.isNullOrEmpty()) {
            TokenInterceptor(token.toString())
        } else {
            null
        }

        val client = if (tokenInterceptor != null) {
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(tokenInterceptor)
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}