package com.android.playground.wear

import com.android.playground.wear.detail.NodeUi
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Finding: Screen()

    @Serializable
    data class Detail(val node: NodeUi): Screen()
}