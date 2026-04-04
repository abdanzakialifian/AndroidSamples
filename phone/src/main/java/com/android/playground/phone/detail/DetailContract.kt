package com.android.playground.phone.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

data class DetailUiState(
    val nodeId: String = "",
    val displayName: String = "",
    val isNearby: Boolean = false,
    val isConnected: Boolean = false,
)

sealed interface DetailIntent {
    data class RequestConnect(val nodeId: String) : DetailIntent
    data class SetNodeOfWatch(val node: NodeUi) : DetailIntent
    data object OnNavigateBack : DetailIntent
}

sealed interface DetailEffect {
    data object NavigateBack : DetailEffect
}

@Serializable
@Parcelize
data class NodeUi(
    val id: String,
    val displayName: String,
    val isNearby: Boolean,
) : Parcelable