package com.android.playground.watch.presentation.discoverable

data class DiscoverableUiState(
    val isConnected: Boolean = false
)

sealed interface DiscoverableIntent {
    data object AddLocalCapability : DiscoverableIntent
    data object RemoveLocalCapability : DiscoverableIntent
    data object AddMessageReceivedListener : DiscoverableIntent
    data object RemoveMessageReceivedListener : DiscoverableIntent
    data object AddRpcServiceListener : DiscoverableIntent
    data object RemoveRpcServiceListener : DiscoverableIntent
}

sealed interface DiscoverableEffect {
    data object GoToNextScreen : DiscoverableEffect
}