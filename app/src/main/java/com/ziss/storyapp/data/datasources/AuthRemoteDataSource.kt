package com.ziss.storyapp.data.datasources

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.ziss.storyapp.Injection.provideApiService
import com.ziss.storyapp.data.datasources.service.ApiService
import com.ziss.storyapp.data.models.BaseResponse
import com.ziss.storyapp.data.models.LoginResponse
import com.ziss.storyapp.utils.ResultState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface AuthRemoteDataSource {
    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>>
    fun register(name: String, email: String, password: String): LiveData<ResultState<BaseResponse>>
}

class AuthRemoteDataSourceImpl private constructor(private val apiService: ApiService) :
    AuthRemoteDataSource {

    private val loginResult = MediatorLiveData<ResultState<LoginResponse>>()
    private val registerResult = MediatorLiveData<ResultState<BaseResponse>>()

    override fun login(email: String, password: String): LiveData<ResultState<LoginResponse>> {
        loginResult.value = ResultState.Loading

        val client = apiService.login(email, password)

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null) {
                    loginResult.value = ResultState.Success(responseBody)
                } else {
                    loginResult.value = ResultState.Failed(response.message().toString())
                    Log.d(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                loginResult.value = ResultState.Failed(t.message.toString())
                Log.d(TAG, t.message.toString())
            }
        })

        return loginResult
    }


    override fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultState<BaseResponse>> {
        registerResult.value = ResultState.Loading

        val client = apiService.register(name, email, password)

        client.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null) {
                    Log.d(TAG, responseBody.message)
                    registerResult.value = ResultState.Success(responseBody)
                } else {
                    registerResult.value = ResultState.Failed(response.message().toString())
                    Log.d(TAG, "else")
                    Log.d(TAG, response.message())
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                registerResult.value = ResultState.Failed(t.message.toString())
                Log.d(TAG, "error")
                Log.d(TAG, t.message.toString())
            }
        })

        return registerResult
    }

    companion object {
        private var TAG = AuthRemoteDataSource::class.java.simpleName
        private var instance: AuthRemoteDataSourceImpl? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AuthRemoteDataSourceImpl(provideApiService())
        }.also { instance = it }
    }
}