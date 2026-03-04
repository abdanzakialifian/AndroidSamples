package com.android.playground.calendar

import com.android.playground.calendar.enum.DayPosition
import com.android.playground.calendar.extensions.asStartMonth
import com.android.playground.calendar.extensions.nextMonth
import com.android.playground.calendar.extensions.previousMonth
import com.android.playground.calendar.extensions.yearMonth
import com.android.playground.calendar.model.CalendarDay
import com.android.playground.calendar.model.CalendarMonth
import java.time.YearMonth

class MonthData(
    private val month: YearMonth,
    inDays: Int,
    outDays: Int
) {
    private val totalDays = inDays + month.lengthOfMonth() + outDays

    private val firstDay = month.asStartMonth().minusDays(inDays.toLong())

    private val rows = (0 until totalDays).chunked(7)

    private val previousMonth = month.previousMonth

    private val nextMonth = month.nextMonth

    val calendarMonth: CalendarMonth = CalendarMonth(
        yearMonth = month,
        weekDays = rows.map { week ->
            week.map { dayOffset ->
                getDay(dayOffset)
            }
        }
    )

    private fun getDay(dayOffset: Int): CalendarDay {
        val date = firstDay.plusDays(dayOffset.toLong())
        val position = when (date.yearMonth) {
            month -> DayPosition.MonthDate
            previousMonth -> DayPosition.InDate
            nextMonth -> DayPosition.OutDate
            else -> DayPosition.MonthDate
        }
        return CalendarDay(date, position)
    }
}