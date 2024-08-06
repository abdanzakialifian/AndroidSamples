package com.kotlin.androidsamples.mockresponseretrofit.utils


sealed class Result<out R> {
    data class SUCCESS<out T>(val data: T) : Result<T>()
    data class ERROR(val exception: Exception): Result<Nothing>()
    data object LOADING : Result<Nothing>()
    data object INITIAL : Result<Nothing>()
}