package com.kotlin.androidsamples.mockresponseretrofit.data.repository

import com.kotlin.androidsamples.mockresponseretrofit.data.network.response.ListUsersResponse
import com.kotlin.androidsamples.mockresponseretrofit.data.network.response.UserResponse
import com.kotlin.androidsamples.mockresponseretrofit.data.network.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MockResponseRetrofitRepositoryImpl(private val apiService: ApiService) :
    MockResponseRetrofitRepository {
    override suspend fun getUsers(): List<ListUsersResponse> {
        return withContext(Dispatchers.IO) {
            apiService.getListUsers()
        }
    }

    override suspend fun getUser(username: String): UserResponse {
        return withContext(Dispatchers.IO) {
            apiService.getUser(username)
        }
    }
}