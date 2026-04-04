package com.android.playground.watch

import android.app.Application
import com.android.playground.watch.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class WatchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(mainModule)
        }
    }
}