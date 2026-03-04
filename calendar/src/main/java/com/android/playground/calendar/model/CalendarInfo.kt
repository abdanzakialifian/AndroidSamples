package com.android.playground.calendar.model

import androidx.compose.runtime.Immutable
import com.android.playground.calendar.enum.OutDateStyle
import java.time.DayOfWeek

@Immutable
data class CalendarInfo(
    val indexCount: Long = 0L,
    private val firstDayOfWeek: DayOfWeek? = null,
    private val outDateStyle: OutDateStyle? = null
)