package com.kotlin.androidsamples.calendarview.model

import androidx.compose.runtime.Immutable
import com.kotlin.androidsamples.calendarview.enum.OutDateStyle
import java.time.DayOfWeek

@Immutable
data class CalendarInfo(
    val indexCount: Long = 0L,
    private val firstDayOfWeek: DayOfWeek? = null,
    private val outDateStyle: OutDateStyle? = null
)