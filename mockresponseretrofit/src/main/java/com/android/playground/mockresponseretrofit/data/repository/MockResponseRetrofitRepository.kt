package com.android.playground.mockresponseretrofit.data.repository

import com.android.playground.mockresponseretrofit.data.network.response.ListUsersResponse
import com.android.playground.mockresponseretrofit.data.network.response.UserResponse

interface MockResponseRetrofitRepository {
    suspend fun getUsers(): List<ListUsersResponse>
    suspend fun getUser(username: String): UserResponse
}