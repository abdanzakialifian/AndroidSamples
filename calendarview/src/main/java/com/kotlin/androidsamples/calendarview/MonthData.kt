package com.kotlin.androidsamples.calendarview

import com.kotlin.androidsamples.calendarview.extensions.asStartMonth
import com.kotlin.androidsamples.calendarview.extensions.daysUntil
import com.kotlin.androidsamples.calendarview.extensions.nextMonth
import com.kotlin.androidsamples.calendarview.extensions.previousMonth
import com.kotlin.androidsamples.calendarview.extensions.yearMonth
import com.kotlin.androidsamples.calendarview.model.CalendarDay
import com.kotlin.androidsamples.calendarview.model.CalendarMonth
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.temporal.ChronoUnit

data class MonthData(
    private val month: YearMonth,
    private val inDays: Int,
    private val outDays: Int
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
            else -> throw IllegalArgumentException("Invalid date : $date in mon : $month")
        }
        return CalendarDay(date, position)
    }
}

fun getCalendarMonthData(
    startMonth: YearMonth,
    offset: Int,
    firstDayOfWeek: DayOfWeek,
    outDateStyle: OutDateStyle,
): MonthData {
    val month = startMonth.plusMonths(offset.toLong())
    val firstDay = month.asStartMonth()
    val inDays = firstDayOfWeek.daysUntil(firstDay.dayOfWeek)
    val outDays = (inDays + month.lengthOfMonth()).let { inAndMonthDays ->
        val endOfRowDays = if (inAndMonthDays % 7 != 0) 7 - (inAndMonthDays % 7) else 0
        val endOfGridDays = if (outDateStyle == OutDateStyle.EndOfRow) {
            0
        } else {
            val weeksInMonth = (inAndMonthDays + endOfRowDays) / 7
            (6 - weeksInMonth) * 7
        }
        return@let endOfRowDays + endOfGridDays
    }
    return MonthData(month, inDays, outDays)
}

fun getMonthIndex(startMonth: YearMonth, targetMonth: YearMonth): Long {
    return ChronoUnit.MONTHS.between(startMonth, targetMonth)
}

fun getMonthIndicesCount(startMonth: YearMonth, endMonth: YearMonth): Long {
    return getMonthIndex(startMonth, endMonth) + 1
}