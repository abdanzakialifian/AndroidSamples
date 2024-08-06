package com.kotlin.androidsamples.mockresponseretrofit.presentation

import androidx.lifecycle.ViewModel
import com.kotlin.androidsamples.mockresponseretrofit.data.network.response.ListUsersResponse
import com.kotlin.androidsamples.mockresponseretrofit.data.network.response.UserResponse
import com.kotlin.androidsamples.mockresponseretrofit.data.repository.MockResponseRetrofitRepository
import com.kotlin.androidsamples.mockresponseretrofit.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockResponseRetrofitViewModel(private val mockResponseRetrofitRepository: MockResponseRetrofitRepository) :
    ViewModel() {
    private val _listUsers: MutableStateFlow<Result<List<ListUsersResponse>>> =
        MutableStateFlow(Result.INITIAL)
    val listUsers get() = _listUsers.asStateFlow()

    private val _user: MutableStateFlow<Result<UserResponse>> = MutableStateFlow(Result.INITIAL)
    val user get() = _user.asStateFlow()

    suspend fun getUsers() {
        _user.emit(Result.LOADING)
        try {
            _listUsers.emit(Result.SUCCESS(mockResponseRetrofitRepository.getUsers()))
        } catch (e: Exception) {
            _listUsers.emit(Result.ERROR(e))
        }
    }

    suspend fun getUser(username: String) {
        _user.emit(Result.LOADING)
        try {
            _user.emit(Result.SUCCESS(mockResponseRetrofitRepository.getUser(username)))
        } catch (e: Exception) {
            _user.emit(Result.ERROR(e))
        }
    }
}