package com.kotlin.androidsamples.mockresponseretrofit.utils

import android.content.Context
import okio.EOFException
import java.io.BufferedReader

object Utils {
    fun readJsonFile(context: Context, fileName: String): String {
        return try {
            context
                .assets
                .open(fileName)
                .bufferedReader()
                .use(BufferedReader::readText)
        } catch (e: EOFException) {
            ""
        }
    }
}