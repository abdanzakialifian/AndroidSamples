package com.kotlin.androidsamples.mockresponseretrofit

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kotlin.androidsamples.mockresponseretrofit.network.okhttp.ApiConfig
import com.kotlin.androidsamples.mockresponseretrofit.network.response.ListUsersResponse
import com.kotlin.androidsamples.mockresponseretrofit.network.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MockResponseRetrofitViewModel : ViewModel() {
    private val _listUsers: MutableStateFlow<List<ListUsersResponse>> =
        MutableStateFlow(emptyList())
    val listUsers get() = _listUsers.asStateFlow()

    private val _user: MutableStateFlow<UserResponse?> = MutableStateFlow(null)
    val user get() = _user.asStateFlow()

    fun getUsers(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _listUsers.value = ApiConfig.apiService(context).getListUsers()
                } catch (e: HttpException) {
                    Log.d("CEK", "EXCEPTION : $e")
                }
            }
        }
    }

    fun getUser(context: Context, username: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    _user.value = ApiConfig.apiService(context).getUser(username)
                } catch (e: HttpException) {
                    Log.d("CEK", "EXCEPTION : $e")
                }
            }
        }
    }
}