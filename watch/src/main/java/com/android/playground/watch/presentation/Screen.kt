package com.android.playground.watch.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    object Landing: Screen("landing_screen")

    @Serializable
    object Discoverable: Screen("connect_screen")

    @Serializable
    object Dashboard : Screen("dashboard_screen/{${ScreenPath.DEVICE_INFO}}") {
        fun createRoute(deviceInfoJson: String) = "dashboard_screen/$deviceInfoJson"
    }
}

object ScreenPath {
    const val DEVICE_INFO = "deviceInfo"
}