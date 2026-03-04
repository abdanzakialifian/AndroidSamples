package com.android.playground.mockokhttp.data.repository

import com.android.playground.mockokhttp.data.network.response.ListUsersResponse
import com.android.playground.mockokhttp.data.network.response.UserResponse

interface MockResponseRetrofitRepository {
    suspend fun getUsers(): List<ListUsersResponse>
    suspend fun getUser(username: String): UserResponse
}