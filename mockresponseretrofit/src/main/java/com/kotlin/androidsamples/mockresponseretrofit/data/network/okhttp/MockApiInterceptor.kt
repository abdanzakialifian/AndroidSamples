package com.kotlin.androidsamples.mockresponseretrofit.data.network.okhttp

import android.content.Context
import com.kotlin.androidsamples.mockresponseretrofit.utils.MOCK
import com.kotlin.androidsamples.mockresponseretrofit.utils.Utils
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Invocation

/**
 * An [Interceptor] implementation for mocking API responses in tests.
 *
 * This interceptor checks if the request has a specific annotation (`MOCK`). If the annotation is present,
 * it reads a JSON file specified by the annotation and returns a mock response with that JSON content.
 * If the JSON file is not found or is blank, it returns a 404 Not Found response. If the annotation is not present,
 * it proceeds with the original request.
 *
 * @param context The [Context] used to read JSON files from the file system or resources.
 */
class MockApiInterceptor(private val context: Context) : Interceptor {
    /**
     * Intercepts the request and provides a mock response if the request has the `MOCK` annotation.
     *
     * Checks the request for a `MOCK` annotation. If found, it reads the corresponding JSON file and creates a
     * mock response with the JSON content. If the JSON is blank or the file is not found, it returns a 404 response.
     * If the annotation is not present, it forwards the original request to the next interceptor or network call.
     *
     * @param chain The [Interceptor.Chain] to proceed with or mock the response.
     * @return A [Response] object, either mocked with JSON content or the original response.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val chainRequest = chain.request()

        val chainResponse = chain.proceed(chainRequest)

        return try {
            val invocation =
                chain.request().tag(Invocation::class.java) ?: return chainResponse

            // Find the MOCK annotation
            val annotation = invocation.method().annotations.toSet().firstOrNull { annotation ->
                annotation is MOCK
            }

            if (annotation is MOCK) {
                // Read JSON from file specified in the annotation
                val json = Utils.readJsonFile(context, annotation.fileName)

                if (json.isNotBlank()) {
                    Response.Builder()
                        .protocol(Protocol.HTTP_2) // Set the HTTP protocol version
                        .headers(chainResponse.headers) // Retain original response headers
                        .code(200) // Success status code
                        .message("Success Mocked") // Custom message
                        .body(json.toResponseBody("application/json; charset=utf-8".toMediaType())) // Mocked JSON body
                        .request(chainRequest) // Original request
                        .build()
                } else {
                    Response.Builder()
                        .protocol(Protocol.HTTP_2) // Set the HTTP protocol version
                        .headers(chainResponse.headers) // Retain original response headers
                        .code(404) // Failed status code
                        .message("Failed Mocked") // Custom message
                        .request(chainRequest) // Original request
                        .build()
                }
            } else {
                chainResponse // Proceed with the original response
            }
        } catch (e: Exception) {
            chainResponse // In case of an exception, return the original response
        }
    }
}