package com.kotlin.androidsamples.calendarview.model

import androidx.compose.runtime.Immutable
import com.kotlin.androidsamples.calendarview.DayPosition
import java.io.Serializable
import java.time.LocalDate

@Immutable
data class CalendarDay(val date: LocalDate, val position: DayPosition): Serializable