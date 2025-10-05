package com.kotlin.androidsamples.calendarview

import com.kotlin.androidsamples.calendarview.enum.DayPosition
import com.kotlin.androidsamples.calendarview.extensions.asStartMonth
import com.kotlin.androidsamples.calendarview.extensions.nextMonth
import com.kotlin.androidsamples.calendarview.extensions.previousMonth
import com.kotlin.androidsamples.calendarview.extensions.yearMonth
import com.kotlin.androidsamples.calendarview.model.CalendarDay
import com.kotlin.androidsamples.calendarview.model.CalendarMonth
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