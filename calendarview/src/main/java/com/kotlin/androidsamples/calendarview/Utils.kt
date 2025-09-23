package com.kotlin.androidsamples.calendarview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import com.kotlin.androidsamples.calendarview.Utils.completelyVisibleMonth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.WeekFields
import java.util.Locale

object Utils {
    fun firstDatOfWeekFromLocale(locale: Locale = Locale.getDefault()): DayOfWeek = WeekFields.of(locale).firstDayOfWeek

    fun daysOfWeek(firstDayOfWeek: DayOfWeek = firstDatOfWeekFromLocale()): List<DayOfWeek> {
        val pivot = 7 - firstDayOfWeek.ordinal
        val dayOfWeek = DayOfWeek.entries
        return dayOfWeek.takeLast(pivot) + dayOfWeek.dropLast(pivot)
    }

    fun YearMonth.asStartMonth(): LocalDate = this.atDay(1)

    val LocalDate.yearMonth: YearMonth get() = YearMonth.of(year, month)

    val YearMonth.nextMonth: YearMonth get() = plusMonths(1)

    val YearMonth.previousMonth: YearMonth get() = minusMonths(1)

    fun <T : Comparable<T>> checkRange(start: T, end: T) {
        check(end >= start) {
            "start : $start is greater than end : $end"
        }
    }

    fun DayOfWeek.daysUntil(other: DayOfWeek): Int = (7 + (other.ordinal - ordinal)) % 7

    val CalendarDay.positionYearMonth: YearMonth
        get() = when (position) {
            DayPosition.InDate -> date.yearMonth.nextMonth
            DayPosition.MonthDate -> date.yearMonth
            DayPosition.OutDate -> date.yearMonth.previousMonth
        }

    inline fun <T> Iterable<T>.indexOfFirstOrNull(predicate: (T) -> Boolean): Int? {
        val result = indexOfFirst(predicate)
        return if (result == -1) null else result
    }

    val CalendarLayoutInfo.completelyVisibleMonth: List<CalendarMonth>
        get() {
            val visibleItemsInfo = visibleMonthsInfo.toMutableList()
            return if (visibleItemsInfo.isEmpty()) {
                emptyList()
            } else {
                val lastItem = visibleItemsInfo.last()
                val viewportSize = viewportEndOffset + viewportStartOffset
                if (lastItem.offset + lastItem.size > viewportSize) {
                    visibleItemsInfo.removeAt(visibleItemsInfo.lastIndex)
                }
                val firstItem = visibleItemsInfo.firstOrNull()
                if (firstItem != null && firstItem.offset < viewportStartOffset) {
                    visibleItemsInfo.removeAt(0)
                }
                visibleItemsInfo.map { it.month }
            }
        }

    fun YearMonth.displayText(short: Boolean = false): String = "${month.displayText(short)} $year"

    fun Month.displayText(short: Boolean = true): String {
        val style = if (short) TextStyle.SHORT else TextStyle.FULL
        return getDisplayName(style, Locale.ENGLISH)
    }

    fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String {
        val style = if (narrow) TextStyle.NARROW else TextStyle.SHORT
        return getDisplayName(style, Locale.ENGLISH).let { value ->
            if (uppercase) value.uppercase(Locale.ENGLISH) else value
        }
    }
}

@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.completelyVisibleMonth.firstOrNull() }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}