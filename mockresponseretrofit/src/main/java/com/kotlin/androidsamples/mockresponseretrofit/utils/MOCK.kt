package com.kotlin.androidsamples.mockresponseretrofit.utils

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class MOCK(val fileName: String)