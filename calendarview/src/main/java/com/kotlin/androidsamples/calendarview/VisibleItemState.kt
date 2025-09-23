package com.kotlin.androidsamples.calendarview

import androidx.compose.runtime.Immutable
import java.io.Serializable

@Immutable
data class VisibleItemState(val firstVisibleItemIndex: Int = 0, val firstVisibleItemScrollOffset: Int = 0) : Serializable