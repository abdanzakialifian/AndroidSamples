package com.kotlin.androidsamples.mockresponseretrofit.network.okhttp

import android.content.Context
import com.kotlin.androidsamples.mockresponseretrofit.utils.Utils
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Invocation

class MockApiInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return try {
            val invocation =
                chain.request().tag(Invocation::class.java) ?: return chain.proceed(chain.request())
            val annotation = invocation.method().annotations.toSet().firstOrNull { annotation ->
                annotation is MOCK
            }

            if (annotation is MOCK) {
                val json = Utils.readJsonFile(context, annotation.fileName)
                if (json.isNotBlank()) {
                    Response.Builder()
                        .code(200)
                        .protocol(Protocol.HTTP_2)
                        .message(json)
                        .body(
                            json.toByteArray()
                                .toResponseBody("application/json".toMediaTypeOrNull())
                        )
                        .request(chain.request())
                        .build()
                } else {
                    Response.Builder()
                        .code(404)
                        .protocol(Protocol.HTTP_2)
                        .message("Page Not Found")
                        .request(chain.request())
                        .build()
                }
            } else {
                chain.proceed(chain.request())
            }
        } catch (e: Exception) {
            chain.proceed(chain.request())
        }
    }
}