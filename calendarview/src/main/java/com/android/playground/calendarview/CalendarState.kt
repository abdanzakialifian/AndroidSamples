package com.android.playground.calendarview

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.android.playground.calendarview.enum.OutDateStyle
import com.android.playground.calendarview.extensions.asStartMonth
import com.android.playground.calendarview.extensions.daysUntil
import com.android.playground.calendarview.model.CalendarInfo
import com.android.playground.calendarview.model.CalendarMonth
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@Stable
class CalendarState(
    startMonth: YearMonth,
    endMonth: YearMonth,
    firstDayOfWeek: DayOfWeek,
    firstVisibleMonth: YearMonth,
    outDateStyle: OutDateStyle,
) : ScrollableState {
    var startMonth by mutableStateOf(startMonth)
        private set

    var endMonth by mutableStateOf(endMonth)
        private set

    var firstDayOfWeek by mutableStateOf(firstDayOfWeek)
        private set

    var outDateStyle by mutableStateOf(outDateStyle)
        private set

    val listState = LazyListState(firstVisibleItemIndex = getScrollIndex(firstVisibleMonth).toInt())

    val firstVisibleMonth: CalendarMonth by derivedStateOf { store[listState.firstVisibleItemIndex] }

    val layoutInfo: CalendarLayoutInfo
        get() = CalendarLayoutInfo(listState.layoutInfo) { index ->
            store[index]
        }

    var calendarInfo by mutableStateOf(CalendarInfo())

    val store = DataStore { offset ->
        calendarMonthData(
            startMonth = this.startMonth,
            offset = offset,
            firstDayOfWeek = this.firstDayOfWeek,
            outDateStyle = this.outDateStyle
        ).calendarMonth
    }

    init {
        monthDataChanged()
    }

    private fun monthDataChanged() {
        store.clear()
        calendarInfo = CalendarInfo(
            indexCount = getMonthIndex(startMonth, endMonth) + 1,
            firstDayOfWeek = firstDayOfWeek,
            outDateStyle = outDateStyle,
        )
    }

    private fun calendarMonthData(
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
            endOfRowDays + endOfGridDays
        }
        return MonthData(month = month, inDays = inDays, outDays = outDays)
    }

    private fun getMonthIndex(startMonth: YearMonth, targetMonth: YearMonth): Long {
        return ChronoUnit.MONTHS.between(startMonth, targetMonth)
    }

    suspend fun animateScrollToMonth(month: YearMonth) {
        listState.animateScrollToItem(getScrollIndex(month).toInt())
    }

    private fun getScrollIndex(month: YearMonth): Long {
        if (month !in startMonth..endMonth) return 0L
        return getMonthIndex(startMonth, month)
    }

    override val isScrollInProgress: Boolean
        get() = listState.isScrollInProgress

    override fun dispatchRawDelta(delta: Float): Float = listState.dispatchRawDelta(delta)

    override suspend fun scroll(scrollPriority: MutatePriority, block: suspend ScrollScope.() -> Unit) {
        listState.scroll(scrollPriority, block)
    }

    companion object {
        internal val Saver: Saver<CalendarState, Any> = listSaver(
            save = {
                listOf(
                    it.startMonth,
                    it.endMonth,
                    it.firstVisibleMonth.yearMonth,
                    it.firstDayOfWeek,
                    it.outDateStyle,
                    it.listState.firstVisibleItemIndex,
                    it.listState.firstVisibleItemScrollOffset
                )
            },
            restore = {
                CalendarState(
                    startMonth = it.getOrNull(0) as? YearMonth ?: YearMonth.now(),
                    endMonth = it.getOrNull(1) as? YearMonth ?: YearMonth.now(),
                    firstVisibleMonth = it.getOrNull(2) as? YearMonth ?: YearMonth.now(),
                    firstDayOfWeek = it.getOrNull(3) as? DayOfWeek ?: Utils.daysOfWeek().first(),
                    outDateStyle = it.getOrNull(4) as? OutDateStyle ?: OutDateStyle.EndOfGrid,
                )
            }
        )
    }
}

@Composable
fun rememberCalendarState(
    startMonth: YearMonth = YearMonth.now(),
    endMonth: YearMonth = startMonth,
    firstVisibleMonth: YearMonth = startMonth,
    firstDayOfWeek: DayOfWeek = Utils.daysOfWeek().first(),
    outDateStyle: OutDateStyle = OutDateStyle.EndOfRow
): CalendarState {
    return rememberSaveable(
        startMonth,
        endMonth,
        firstVisibleMonth,
        firstDayOfWeek,
        outDateStyle,
        saver = CalendarState.Saver
    ) {
        CalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = firstVisibleMonth,
            firstDayOfWeek = firstDayOfWeek,
            outDateStyle = outDateStyle,
        )
    }
}