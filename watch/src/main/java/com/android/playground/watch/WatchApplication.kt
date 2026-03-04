package com.android.playground.watch

import android.app.Application

class WatchApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.getDeviceInformation()
    }
}