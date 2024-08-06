package com.kotlin.androidsamples.mockresponseretrofit.presentation

import android.app.Application
import com.kotlin.androidsamples.mockresponseretrofit.data.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MockResponseRetrofitApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MockResponseRetrofitApp)
            modules(appModule)
        }
    }
}