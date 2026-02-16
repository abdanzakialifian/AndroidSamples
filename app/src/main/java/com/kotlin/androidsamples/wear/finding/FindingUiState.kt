package com.kotlin.androidsamples.wear.finding

import androidx.compose.runtime.Immutable
import com.google.android.gms.wearable.Node

@Immutable
data class FindingUiState(
    val nodes: Set<Node> = emptySet(),
    val time: Int = 15,
) {
    val minutes get() = time / 60
    val seconds get() = time % 60
}
