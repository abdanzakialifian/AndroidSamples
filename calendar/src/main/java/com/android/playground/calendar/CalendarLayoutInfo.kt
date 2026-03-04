package com.android.playground.calendar

import androidx.compose.foundation.lazy.LazyListLayoutInfo
import com.android.playground.calendar.model.CalendarMonth

class CalendarLayoutInfo(info: LazyListLayoutInfo, private val month: (Int) -> CalendarMonth) : LazyListLayoutInfo by info {
    val visibleMonthsInfo: List<Triple<Int, Int, CalendarMonth>>
        get() = visibleItemsInfo.map {
            Triple(it.offset, it.size, month(it.index))
        }
}