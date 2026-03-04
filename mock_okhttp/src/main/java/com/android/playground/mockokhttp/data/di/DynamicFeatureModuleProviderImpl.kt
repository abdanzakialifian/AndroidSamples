package com.android.playground.mockokhttp.data.di

import android.content.Context
import com.android.playground.di.DynamicFeatureModuleProvider
import com.android.playground.mockohttp.BuildConfig
import com.android.playground.mockokhttp.data.network.okhttp.MockApiInterceptor
import com.android.playground.mockokhttp.data.network.retrofit.ApiService
import com.android.playground.mockokhttp.data.repository.MockResponseRetrofitRepository
import com.android.playground.mockokhttp.data.repository.MockResponseRetrofitRepositoryImpl
import com.android.playground.mockokhttp.presentation.MockResponseRetrofitViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DynamicFeatureModuleProviderImpl : DynamicFeatureModuleProvider {
    override fun getKoinModules(): List<Module> = listOf(
        module {
            single { provideApiService(get()) }

            single<MockResponseRetrofitRepository> { MockResponseRetrofitRepositoryImpl(get()) }

            viewModel { MockResponseRetrofitViewModel(get()) }
        }
    )

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
}