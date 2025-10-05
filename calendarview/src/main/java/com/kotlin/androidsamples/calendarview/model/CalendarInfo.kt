package com.kotlin.androidsamples.calendarview.model

import androidx.compose.runtime.Immutable
import com.kotlin.androidsamples.calendarview.OutDateStyle
import java.time.DayOfWeek

@Immutable
data class CalendarInfo(
    val indexCount: Long,
    private val firstDayOfWeek: DayOfWeek? = null,
    private val outDateStyle: OutDateStyle? = null
)