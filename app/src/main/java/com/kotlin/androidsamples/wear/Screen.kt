package com.kotlin.androidsamples.wear

import com.kotlin.androidsamples.wear.detail.NodeUi
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    object Finding: Screen()

    @Serializable
    data class Detail(val node: NodeUi): Screen()
}