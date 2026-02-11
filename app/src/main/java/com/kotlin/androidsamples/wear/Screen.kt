package com.kotlin.androidsamples.wear

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(val route: String) {
    @Serializable
    object Finding: Screen("finding_screen")
}