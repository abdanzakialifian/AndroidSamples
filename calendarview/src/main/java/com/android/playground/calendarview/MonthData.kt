package com.android.playground.calendarview

import com.android.playground.calendarview.enum.DayPosition
import com.android.playground.calendarview.extensions.asStartMonth
import com.android.playground.calendarview.extensions.nextMonth
import com.android.playground.calendarview.extensions.previousMonth
import com.android.playground.calendarview.extensions.yearMonth
import com.android.playground.calendarview.model.CalendarDay
import com.android.playground.calendarview.model.CalendarMonth
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