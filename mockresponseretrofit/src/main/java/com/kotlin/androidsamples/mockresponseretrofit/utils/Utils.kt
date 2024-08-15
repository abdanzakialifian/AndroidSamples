package com.kotlin.androidsamples.mockresponseretrofit.utils

import android.content.Context
import okio.EOFException
import retrofit2.HttpException
import java.io.BufferedReader
import java.io.IOException

object Utils {
    fun readAssetFileWithMessageException(context: Context, fileName: String): String {
        return try {
            context
                .assets
                .open(fileName)
                .bufferedReader()
                .use(BufferedReader::readText)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun Exception.handleResponseError() {
        when(this) {
            is HttpException -> {
                when(this.code()) {
                    404 -> {}
                    500 -> {}
                    else -> {}
                }
            }

            is IOException -> {}

            else -> {}
        }
    }
}