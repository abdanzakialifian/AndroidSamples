package com.kotlin.androidsamples.calendarview.extensions

import com.kotlin.androidsamples.calendarview.CalendarLayoutInfo
import com.kotlin.androidsamples.calendarview.model.CalendarMonth
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

val CalendarLayoutInfo.completelyVisibleMonth: List<CalendarMonth>
    get() {
        val visibleItemsInfo = visibleMonthsInfo.toMutableList()
        return if (visibleItemsInfo.isEmpty()) {
            emptyList()
        } else {
            val firstItem = visibleItemsInfo.firstOrNull()
            val firstOffset = firstItem?.first ?: 0
            if (firstItem != null && firstOffset < viewportStartOffset) {
                visibleItemsInfo.removeFirstOrNull()
            }
            val lastItem = visibleItemsInfo.lastOrNull()
            val lastOffset = lastItem?.first ?: 0
            val lastSize = lastItem?.second ?: 0
            val viewportSize = viewportStartOffset + viewportEndOffset
            if (lastOffset + lastSize > viewportSize) {
                visibleItemsInfo.removeLastOrNull()
            }
            visibleItemsInfo.map { (_, _, calendarMonth) -> calendarMonth }
        }
    }

fun YearMonth.asStartMonth(): LocalDate = this.atDay(1)

val LocalDate.yearMonth: YearMonth get() = YearMonth.of(year, month)

val YearMonth.nextMonth: YearMonth get() = plusMonths(1)

val YearMonth.previousMonth: YearMonth get() = minusMonths(1)

fun DayOfWeek.daysUntil(other: DayOfWeek): Int = (7 + (other.ordinal - ordinal)) % 7