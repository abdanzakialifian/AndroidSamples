package com.android.playground.mockresponseretrofit.data.network.retrofit

import com.android.playground.mockresponseretrofit.utils.MOCK
import com.android.playground.mockresponseretrofit.data.network.response.ListUsersResponse
import com.android.playground.mockresponseretrofit.data.network.response.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("users")
    suspend fun getListUsers(): List<ListUsersResponse>

    @GET("user/{username}")
    @MOCK("user.json")
    suspend fun getUser(
        @Path("username") username: String,
    ): UserResponse
}