package com.android.playground.calendarview

import java.time.DayOfWeek
import java.time.temporal.WeekFields
import java.util.Locale

object Utils {
    fun daysOfWeek(firstDayOfWeek: DayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek): List<DayOfWeek> {
        val pivot = 7 - firstDayOfWeek.ordinal
        val dayOfWeek = DayOfWeek.entries
        return dayOfWeek.takeLast(pivot) + dayOfWeek.dropLast(pivot)
    }
}