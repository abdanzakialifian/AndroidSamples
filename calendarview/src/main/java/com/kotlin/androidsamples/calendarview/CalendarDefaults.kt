package com.kotlin.androidsamples.calendarview

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

internal object CalendarDefaults {
    @Composable
    private fun pagedFlingBehavior(state: LazyListState): FlingBehavior {
        val snappingLayout = remember(state) {
            val provider = SnapLayoutInfoProvider(state, SnapPosition.Start)
            calendarSnapLayoutInfoProvider(provider)
        }
        return rememberSnapFlingBehavior(snappingLayout)
    }

    @Composable
    private fun continuesFlingBehavior(): FlingBehavior = ScrollableDefaults.flingBehavior()

    @Composable
    fun flingBehavior(isPaged: Boolean, state: LazyListState): FlingBehavior = if (isPaged) pagedFlingBehavior(state) else continuesFlingBehavior()

    private fun calendarSnapLayoutInfoProvider(snapLayoutInfoProvider: SnapLayoutInfoProvider): SnapLayoutInfoProvider =
        object : SnapLayoutInfoProvider by snapLayoutInfoProvider {
            override fun calculateApproachOffset(velocity: Float, decayOffset: Float): Float = 0F
        }
}