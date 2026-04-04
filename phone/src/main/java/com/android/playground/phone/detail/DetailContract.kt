package com.android.playground.phone.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

data class DetailUiState(
    val nodeId: String = "",
    val displayName: String = "",
    val isNearby: Boolean = false,
    val isLoading: Boolean = false,
)

sealed interface DetailIntent {
    data class RequestConnect(val nodeId: String) : DetailIntent
    data class LoadNode(val node: NodeUi) : DetailIntent
    data object OnGoToBackScreen : DetailIntent
}

sealed interface DetailEffect {
    data class GoToDashboardScreen(val deviceInfoJson: String) : DetailEffect
    data object GoToBackScreen : DetailEffect
}

@Serializable
@Parcelize
data class NodeUi(
    val id: String,
    val displayName: String,
    val isNearby: Boolean,
) : Parcelable