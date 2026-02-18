package com.kotlin.androidsamples.wear.finding

sealed interface FindingIntent {
    data object StartCountdown : FindingIntent
    data object GetCapability : FindingIntent
    data object AddCapabilityChangedListener : FindingIntent
    data object RemoveCapabilityChangedListener : FindingIntent
}