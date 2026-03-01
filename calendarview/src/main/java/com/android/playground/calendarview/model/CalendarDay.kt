package com.android.playground.calendarview.model

import androidx.compose.runtime.Immutable
import com.android.playground.calendarview.enum.DayPosition
import java.time.LocalDate

@Immutable
data class CalendarDay(val date: LocalDate, val position: DayPosition)