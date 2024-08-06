package com.kotlin.androidsamples.mockresponseretrofit.data.repository

import com.kotlin.androidsamples.mockresponseretrofit.data.network.response.ListUsersResponse
import com.kotlin.androidsamples.mockresponseretrofit.data.network.response.UserResponse

interface MockResponseRetrofitRepository {
    suspend fun getUsers(): List<ListUsersResponse>
    suspend fun getUser(username: String): UserResponse
}