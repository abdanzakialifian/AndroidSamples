package com.android.playground.phone.finding

import androidx.compose.runtime.Immutable
import com.google.android.gms.wearable.Node

sealed interface FindingIntent {
    data object StartCountdown : FindingIntent
    data object GetCapability : FindingIntent
    data object AddCapabilityChangedListener : FindingIntent
    data object RemoveCapabilityChangedListener : FindingIntent
    data class OnNodeSelected(val node: Node) : FindingIntent
}

sealed interface FindingEffect {
    data class GoToDetailScreen(val node: Node) : FindingEffect
}

@Immutable
data class FindingUiState(
    val nodes: Set<Node> = emptySet(),
    val time: Int = 15,
) {
    val minutes get() = time / 60
    val seconds get() = time % 60
}