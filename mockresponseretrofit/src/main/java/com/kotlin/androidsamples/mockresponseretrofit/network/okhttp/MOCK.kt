package com.kotlin.androidsamples.mockresponseretrofit.network.okhttp

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MOCK(val fileName: String)