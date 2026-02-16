package com.example.wear

import android.app.Application

class WearApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Utils.getDeviceInformation()
    }
}