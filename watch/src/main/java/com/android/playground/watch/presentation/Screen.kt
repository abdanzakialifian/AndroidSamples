package com.android.playground.watch.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    object Landing: Screen("landing_screen")

    @Serializable
    object Connect: Screen("connect_screen")
}