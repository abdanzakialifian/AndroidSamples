package com.android.playground.phone

import com.android.playground.phone.detail.NodeUi
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Finding : Screen()

    @Serializable
    data class Detail(val node: NodeUi) : Screen()

    @Serializable
    data class Dashboard(val deviceInfoJson: String) : Screen()
}