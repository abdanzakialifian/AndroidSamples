package com.android.playground.wear.finding

sealed interface FindingIntent {
    data object StartCountdown : FindingIntent
    data object GetCapability : FindingIntent
    data object AddCapabilityChangedListener : FindingIntent
    data object RemoveCapabilityChangedListener : FindingIntent
}