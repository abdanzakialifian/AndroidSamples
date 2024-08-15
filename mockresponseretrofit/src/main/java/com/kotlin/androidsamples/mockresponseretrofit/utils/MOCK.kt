package com.kotlin.androidsamples.mockresponseretrofit.utils

/**
 * Annotation used to mark a function as a mock endpoint for testing purposes.
 *
 * This annotation can be applied to functions to indicate that they are mock endpoints,
 * providing a way to specify mock responses for testing. The mock response can include
 * a file name for the response body, a status code, and optional headers.
 *
 * @property fileName The name of the file that contains the mock response body.
 * This file should be located in the assets directory and will be used to simulate
 * the response content for the mock endpoint.
 *
 * @property statusCode The HTTP status code to return with the mock response.
 * The default value is 200, indicating a successful request. Other status codes
 * can be specified to simulate different scenarios.
 *
 * @property headers Optional HTTP headers to include in the mock response.
 * Multiple headers can be specified as a vararg parameter. Each header should be
 * provided in the format "Header-Name: Header-Value".
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MOCK(
    val fileName: String,
    val statusCode: Int = 200,
    vararg val headers: String = [],
)