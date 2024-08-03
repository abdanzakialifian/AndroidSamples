package com.kotlin.androidsamples

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import org.json.JSONObject

class AppIconChangeService : Service() {
    private var firebaseRemoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    private var enabled: String = "MainActivity"

    private var disabled: String = ""

    override fun onCreate() {
        super.onCreate()

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }

        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val jsonString = Firebase.remoteConfig.getString("launcher_icon")
                val json = JSONObject(jsonString)
                enabled = json.optString("enable")
                disabled = json.optString("disable")
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        changeIcon(enabled, disabled)
    }

    private fun changeIcon(enabled: String, disabled: String) {
        try {
            packageManager.setComponentEnabledSetting(
                ComponentName(
                    this,
                    "$packageName.$enabled"
                ),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )

            packageManager.setComponentEnabledSetting(
                ComponentName(
                    this,
                    "$packageName.$disabled"
                ),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        } catch (e: Exception) {
            Log.d(this::class.java.simpleName, "ERROR : $e")
        }
    }
}