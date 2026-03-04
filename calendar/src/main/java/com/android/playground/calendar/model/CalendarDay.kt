package com.android.playground.calendar.model

import androidx.compose.runtime.Immutable
import com.android.playground.calendar.enum.DayPosition
import java.time.LocalDate

@Immutable
data class CalendarDay(val date: LocalDate, val position: DayPosition)