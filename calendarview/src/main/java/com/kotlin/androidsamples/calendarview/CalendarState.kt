package com.kotlin.androidsamples.calendarview

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
import java.time.DayOfWeek
import java.time.YearMonth

@Stable
class CalendarState(
    startMonth: YearMonth,
    endMonth: YearMonth,
    firstDayOfWeek: DayOfWeek,
    firstVisibleMonth: YearMonth,
    outDateStyle: OutDateStyle,
    visibleItemState: VisibleItemState?,
) : ScrollableState {
    private var _startMonth by mutableStateOf(startMonth)
    var startMonth: YearMonth
        get() = _startMonth
        set(value) {
            if (value != startMonth) {
                _startMonth = value
                monthDataChanged()
            }
        }

    private var _endMonth by mutableStateOf(endMonth)
    var endMonth: YearMonth
        get() = _endMonth
        set(value) {
            if (value != endMonth) {
                _endMonth = value
                monthDataChanged()
            }
        }

    private var _firstDayOfWeek by mutableStateOf(firstDayOfWeek)
    var firstDayOfWeek: DayOfWeek
        get() = _firstDayOfWeek
        set(value) {
            if (value != firstDayOfWeek) {
                _firstDayOfWeek = value
                monthDataChanged()
            }
        }

    private var _outDateStyle by mutableStateOf(outDateStyle)
    var outDateStyle: OutDateStyle
        get() = _outDateStyle
        set(value) {
            if (value != outDateStyle) {
                _outDateStyle = value
                monthDataChanged()
            }
        }

    internal val listState = LazyListState(
        firstVisibleItemIndex = visibleItemState?.firstVisibleItemIndex ?: getScrollIndex(firstVisibleMonth) ?: 0,
        firstVisibleItemScrollOffset = visibleItemState?.firstVisibleItemScrollOffset ?: 0
    )

    val firstVisibleMonth: CalendarMonth by derivedStateOf { store[listState.firstVisibleItemIndex] }

    val layoutInfo: CalendarLayoutInfo
        get() = CalendarLayoutInfo(listState.layoutInfo) { index ->
            store[index]
        }

    internal var calendarInfo by mutableStateOf(CalendarInfo(indexCount = 0))

    internal val store = DataStore { offset ->
        getCalendarMonthData(
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
        Utils.checkRange(startMonth, endMonth)
        calendarInfo = CalendarInfo(
            indexCount = getMonthIndicesCount(startMonth, endMonth),
            firstDayOfWeek = firstDayOfWeek,
            outDateStyle = outDateStyle,
        )
    }

    suspend fun animateScrollToMonth(month: YearMonth) {
        listState.animateScrollToItem(getScrollIndex(month) ?: return)
    }

    private fun getScrollIndex(month: YearMonth): Int? {
        if (month !in startMonth..endMonth) {
            return null
        }
        return getMonthIndex(startMonth, month)
    }

    override val isScrollInProgress: Boolean
        get() = listState.isScrollInProgress

    override fun dispatchRawDelta(delta: Float): Float = listState.dispatchRawDelta(delta)

    override suspend fun scroll(
        scrollPriority: MutatePriority,
        block: suspend ScrollScope.() -> Unit
    ) {
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
                    startMonth = it[0] as YearMonth,
                    endMonth = it[1] as YearMonth,
                    firstVisibleMonth = it[2] as YearMonth,
                    firstDayOfWeek = it[3] as DayOfWeek,
                    outDateStyle = it[4] as OutDateStyle,
                    visibleItemState = VisibleItemState(
                        firstVisibleItemIndex = it[5] as Int,
                        firstVisibleItemScrollOffset = it[6] as Int
                    )
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
    firstDayOfWeek: DayOfWeek = Utils.firstDatOfWeekFromLocale(),
    outDateStyle: OutDateStyle = OutDateStyle.EndOfRow
): CalendarState {
    return rememberSaveable(
        inputs = arrayOf<Any>(startMonth, endMonth, firstVisibleMonth, firstDayOfWeek, outDateStyle),
        saver = CalendarState.Saver
    ) {
        CalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = firstVisibleMonth,
            firstDayOfWeek = firstDayOfWeek,
            outDateStyle = outDateStyle,
            visibleItemState = null
        )
    }
}