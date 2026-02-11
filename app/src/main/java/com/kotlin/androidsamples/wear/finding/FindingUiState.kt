package com.kotlin.androidsamples.wear.finding

import androidx.compose.runtime.Immutable
import com.google.android.gms.wearable.Node

@Immutable
data class FindingUiState(
    val nodes: Set<Node> = emptySet()
)
