package com.android.playground.calendar.model

import androidx.compose.runtime.Immutable
import java.io.Serializable
import java.time.YearMonth

@Immutable
data class CalendarMonth(
    val yearMonth: YearMonth,
    val weekDays: List<List<CalendarDay>>
) : Serializable {
    private val firstDay get() = weekDays.firstOrNull()?.firstOrNull()
    private val lastDay get() = weekDays.lastOrNull()?.lastOrNull()

    override fun equals(other: Any?): Boolean = other is CalendarMonth &&
            yearMonth == other.yearMonth &&
            firstDay == other.firstDay &&
            lastDay == other.lastDay

    override fun hashCode(): Int {
        var result = yearMonth.hashCode()
        result = 31 * result + (firstDay?.hashCode() ?: 0)
        result = 31 * result + (lastDay?.hashCode() ?: 0)
        return result
    }
}