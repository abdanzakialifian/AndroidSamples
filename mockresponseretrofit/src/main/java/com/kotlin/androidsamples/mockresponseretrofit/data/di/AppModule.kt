package com.kotlin.androidsamples.mockresponseretrofit.data.di

import android.content.Context
import com.kotlin.androidsamples.mockresponseretrofit.BuildConfig
import com.kotlin.androidsamples.mockresponseretrofit.presentation.MockResponseRetrofitViewModel
import com.kotlin.androidsamples.mockresponseretrofit.data.network.okhttp.MockApiInterceptor
import com.kotlin.androidsamples.mockresponseretrofit.data.network.retrofit.ApiService
import com.kotlin.androidsamples.mockresponseretrofit.data.repository.MockResponseRetrofitRepository
import com.kotlin.androidsamples.mockresponseretrofit.data.repository.MockResponseRetrofitRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single { provideApiService(androidContext()) }

    single<MockResponseRetrofitRepository> { MockResponseRetrofitRepositoryImpl(get()) }

    viewModel { MockResponseRetrofitViewModel(get()) }
}

private fun provideApiService(context: Context): ApiService {
    val loggingInterceptor = HttpLoggingInterceptor().setLevel(
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    )

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)

    if (BuildConfig.DEBUG) {
        okHttpClient.addInterceptor(MockApiInterceptor(context))
    }

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .client(okHttpClient.build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}