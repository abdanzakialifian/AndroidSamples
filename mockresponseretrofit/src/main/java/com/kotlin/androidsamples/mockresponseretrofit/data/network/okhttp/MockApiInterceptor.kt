package com.kotlin.androidsamples.mockresponseretrofit.data.network.okhttp

import android.content.Context
import com.google.gson.Gson
import com.kotlin.androidsamples.mockresponseretrofit.utils.MOCK
import com.kotlin.androidsamples.mockresponseretrofit.utils.Utils
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
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
            val invocation = chain.request().tag(Invocation::class.java) ?: return chainResponse

            // Find the MOCK annotation
            val annotation = invocation.method().annotations.toSet().firstOrNull { annotation ->
                annotation is MOCK
            }

            if (annotation is MOCK) {
                // Read JSON from file specified in the annotation
                val outputAsset =
                    Utils.readAssetFileWithMessageException(context, annotation.fileName)

                // Parse headers from annotation
                val headersBuilder = Headers.Builder()

                try {
                    // Add headers from annotation
                    annotation.headers.forEach { header ->
                        val (key, value) = header.split(": ", limit = 2)
                        headersBuilder.add(key, value)
                    }
                } catch (e: Exception) {
                    return exceptionMockResponse(
                        headers = headersBuilder.build(),
                        statusCode = 500,
                        content = e.toString(),
                        chainRequest = chainRequest
                    )
                }

                if (isJson(outputAsset)) {
                    successMockResponse(
                        headersBuilder = headersBuilder,
                        statusCode = annotation.statusCode,
                        content = outputAsset,
                        chainRequest = chainRequest
                    )
                } else {
                    exceptionMockResponse(
                        headers = headersBuilder.build(),
                        statusCode = 404,
                        content = outputAsset,
                        chainRequest = chainRequest
                    )
                }
            } else {
                chainResponse // Proceed with the original response
            }
        } catch (e: Exception) {
            chainResponse // In case of an exception, return the original response
        }
    }

    /**
     * Constructs a mock HTTP response to simulate successful scenarios.
     *
     * This function creates a `Response` object that represents a successful mock service response. It includes
     * custom headers, a status code, a message, and a JSON body. The JSON body is set to the provided `content`.
     *
     * The `Response` object configured by this function includes:
     * - HTTP protocol version set to HTTP/2.
     * - Custom headers specified by the `headersBuilder`.
     * - A status code representing a successful response.
     * - A message indicating a successful mock.
     * - A body containing the provided content, formatted as a JSON response.
     *
     * @param headersBuilder The `Headers.Builder` instance used to specify custom headers for the response.
     * @param statusCode The HTTP status code to be used for the response, typically representing success (e.g., 200).
     * @param content A string to be included in the response body as the mocked JSON content.
     * @param chainRequest The original request that triggered this response, used to retain the request details.
     *
     * @return A `Response` object configured to simulate a successful response with the provided parameters.
     *
     * @see [Response]
     * @see [Protocol]
     *
     * val response = successMockResponse(
     *     headersBuilder = headersBuilder,
     *     statusCode = 200,
     *     content = "{\"key\": \"value\"}",
     *     chainRequest = originalRequest
     * )
     */
    private fun successMockResponse(
        headersBuilder: Headers.Builder,
        statusCode: Int,
        content: String,
        chainRequest: Request,
    ): Response {
        return Response.Builder()
            .protocol(Protocol.HTTP_2) // Set the HTTP protocol version
            .headers(headersBuilder.build()) // Custom headers
            .code(statusCode) // Custom status code
            .message(TITLE_SUCCESS_MOCKED) // Custom message
            .body(content.toResponseBody(CONTENT_TYPE_RESPONSE_BODY.toMediaType())) // Mocked JSON body
            .request(chainRequest) // Original request
            .build()
    }

    /**
     * Constructs a mock HTTP response to simulate error conditions.
     *
     * This function builds a `Response` object designed to represent an error scenario in a mock service.
     * It includes custom headers, an error status code, a message, and a JSON body containing error details.
     * The JSON body is created from the provided `content` and other error-specific information.
     *
     * The `ResponseObject` used in the body includes:
     * - A timestamp of the error occurrence.
     * - A trace ID for tracking the error.
     * - Information about the source system and response key.
     * - A message object containing localized error descriptions and titles.
     * - An optional data field that is set to `null` for errors.
     *
     * @param headers The `Headers` object to be included in the response. This contains any custom headers required for the response.
     * @param statusCode The HTTP status code representing the error (e.g., 404, 500).
     * @param content A string describing the error message to be included in the response body.
     * @param chainRequest The original request object that triggered this response, preserving the request details.
     *
     * @return A `Response` object configured to simulate the specified error conditions.
     *
     * val response = exceptionMockResponse(
     *     headers = headers,
     *     statusCode = 500,
     *     content = "An unexpected error occurred.",
     *     chainRequest = originalRequest
     * )
     */
    private fun exceptionMockResponse(
        headers: Headers,
        statusCode: Int,
        content: String,
        chainRequest: Request,
    ): Response {
        val jsonObject = JSONObject()
        jsonObject.put("error_message", content)

        return Response.Builder()
            .protocol(Protocol.HTTP_2) // Set the HTTP protocol version
            .headers(headers) // Custom headers
            .code(statusCode) // Custom status code
            .message(TITLE_FAILED_MOCKED) // Custom message
            .body(jsonObject.toString().toResponseBody(CONTENT_TYPE_RESPONSE_BODY.toMediaType()))
            .request(chainRequest) // Original request
            .build()
    }

    /**
     * Checks if the provided string is a valid JSON object.
     *
     * This function attempts to parse the given string as a JSON object. If the parsing is successful,
     * it returns `true`, indicating that the string is a valid JSON object. If the parsing fails due to
     * a `JSONException` or any other exception, it returns `false`, indicating that the string is not a
     * valid JSON object.
     *
     * @param input The string to be checked for JSON validity.
     * @return `true` if the string is a valid JSON object; `false` otherwise.
     *
     * @throws Exception If the string cannot be parsed as a JSON object.
     *
     * @sample
     * val jsonString = "{\"name\": \"ANDROID SAMPLES\", \"type\": \"ANDROID SAMPLES\"}"
     * val regularString = "ANDROID SAMPLES"
     *
     * println(isJson(jsonString)) // true
     * println(isJson(regularString)) // false
     */
    private fun isJson(input: String): Boolean = try {
        JSONObject(input)
        true
    } catch (e: Exception) {
        false
    }

    companion object {
        private const val CONTENT_TYPE_RESPONSE_BODY = "application/json; charset=utf-8"
        private const val TITLE_SUCCESS_MOCKED = "Success Mocked"
        private const val TITLE_FAILED_MOCKED = "Failed Mocked"
    }
}