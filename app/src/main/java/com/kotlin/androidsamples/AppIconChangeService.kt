package com.kotlin.androidsamples

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

class AppIconChangeService : Service() {
    private var firebaseRemoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    private var dynamicLauncherName = ActivityLauncher.DEFAULT.launcherName

    override fun onCreate() {
        super.onCreate()

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }

        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val launcherName =
                    Firebase.remoteConfig.getString(LAUNCHER_NAME_FIREBASE_REMOTE_CONFIG)
                dynamicLauncherName =
                    if (launcherName.isBlank() ||
                        ActivityLauncher.entries.any {
                            it.launcherName.equals(
                                launcherName,
                                ignoreCase = true
                            )
                        }.not()
                    ) {
                        ActivityLauncher.DEFAULT.launcherName
                    } else {
                        launcherName
                    }
            }
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        setupLauncher(dynamicLauncherName)
    }

    private fun setupLauncher(dynamicLauncherName: String) {
        for (launcher in ActivityLauncher.entries) {
            val isEnabledLauncher =
                launcher.launcherName.equals(dynamicLauncherName, ignoreCase = true)

            packageManager.setComponentEnabledSetting(
                ComponentName(this, launcher.activityPackageLocation),
                if (isEnabledLauncher) PackageManager.COMPONENT_ENABLED_STATE_ENABLED else PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }

    companion object {
        private const val LAUNCHER_NAME_FIREBASE_REMOTE_CONFIG = "launcher_name"
    }
}

enum class ActivityLauncher(val launcherName: String, val activityPackageLocation: String) {
    DEFAULT("DEFAULT", "com.kotlin.androidsamples.MainActivity"),
    NEW_YEAR("NEW YEAR", "com.kotlin.androidsamples.NewYearActivityAlias"),
    EID("EID", "com.kotlin.androidsamples.EidActivityAlias")
}