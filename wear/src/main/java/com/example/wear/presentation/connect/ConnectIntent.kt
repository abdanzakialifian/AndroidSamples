package com.example.wear.presentation.connect

sealed interface ConnectIntent {
    data object AddLocalCapability : ConnectIntent
    data object RemoveLocalCapability : ConnectIntent
    data object AddMessageReceivedListener : ConnectIntent
    data object RemoveMessageReceivedListener : ConnectIntent
}