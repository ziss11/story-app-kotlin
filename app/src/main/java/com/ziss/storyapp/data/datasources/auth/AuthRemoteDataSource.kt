package com.ziss.storyapp.data.datasources.auth

import android.util.Log
import androidx.lifecycle.liveData
import com.ziss.storyapp.data.datasources.utils.service.ApiService
import com.ziss.storyapp.utils.Constants
import com.ziss.storyapp.utils.ResultState
import com.ziss.storyapp.utils.wrapEspressoIdlingResource

//interface AuthRemoteDataSource {
//    fun login(email: String, password: String): LiveData<ResultState<LoginResponse>>
//    fun register(name: String, email: String, password: String): LiveData<ResultState<BaseResponse>>
//}

class AuthRemoteDataSource private constructor(private val apiService: ApiService) {

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.login(email, password)
                emit(ResultState.Success(response))
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString()))
            }
        }
    }

    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)

        wrapEspressoIdlingResource {
            try {
                val response = apiService.register(name, email, password)
                emit(ResultState.Success(response))
            } catch (e: Exception) {
                emit(ResultState.Failed(e.message.toString()))
            }
        }
        Log.d("DataSource URL", Constants.BASE_URL)

    }

    companion object {
        private var instance: AuthRemoteDataSource? = null

        fun getInstance(apiService: ApiService) = instance ?: synchronized(this) {
            instance ?: AuthRemoteDataSource(apiService)
        }.also { instance = it }
    }
}