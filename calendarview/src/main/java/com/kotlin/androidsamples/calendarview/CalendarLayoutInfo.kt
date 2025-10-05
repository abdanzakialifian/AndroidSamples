package com.kotlin.androidsamples.calendarview

import androidx.compose.foundation.lazy.LazyListLayoutInfo
import com.kotlin.androidsamples.calendarview.model.CalendarMonth

class CalendarLayoutInfo(info: LazyListLayoutInfo, private val month: (Int) -> CalendarMonth) : LazyListLayoutInfo by info {
    val visibleMonthsInfo: List<Triple<Int, Int, CalendarMonth>>
        get() = visibleItemsInfo.map {
            Triple(it.offset, it.size, month(it.index))
        }
}