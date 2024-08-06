package com.kotlin.androidsamples.mockresponseretrofit.network.retrofit

import com.kotlin.androidsamples.mockresponseretrofit.utils.MOCK
import com.kotlin.androidsamples.mockresponseretrofit.network.response.ListUsersResponse
import com.kotlin.androidsamples.mockresponseretrofit.network.response.UserResponse
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