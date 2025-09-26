package com.kotlin.androidsamples.calendarview.component

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotlin.androidsamples.calendarview.CalendarDay
import com.kotlin.androidsamples.calendarview.CalendarDefaults
import com.kotlin.androidsamples.calendarview.CalendarMonth
import com.kotlin.androidsamples.calendarview.CalendarState
import com.kotlin.androidsamples.calendarview.ContentHeightMode
import com.kotlin.androidsamples.calendarview.rememberCalendarState

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    state: CalendarState = rememberCalendarState(),
    calendarScrollPaged: Boolean = true,
    userScrollEnabled: Boolean = true,
    reserveLayout: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    contentHeightMode: ContentHeightMode = ContentHeightMode.WRAP,
    monthHeader: (@Composable ColumnScope.(CalendarMonth) -> Unit)? = null,
    monthBody: (@Composable ColumnScope.(CalendarMonth, content: @Composable () -> Unit) -> Unit)? = null,
    monthFooter: (@Composable ColumnScope.(CalendarMonth) -> Unit)? = null,
    monthContainer: (@Composable LazyItemScope.(CalendarMonth, container: @Composable () -> Unit) -> Unit)? = null,
    dayContent: @Composable BoxScope.(CalendarDay) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        state = state.listState,
        flingBehavior = CalendarDefaults.flingBehavior(calendarScrollPaged, state.listState),
        userScrollEnabled = userScrollEnabled,
        reverseLayout = reserveLayout,
        contentPadding = contentPadding
    ) {
        CalendarMonths(
            monthCount = state.calendarInfo.indexCount,
            contentHeightMode = contentHeightMode,
            monthData = state.store::get,
            dayContent = dayContent,
            monthHeader = monthHeader,
            monthBody = monthBody,
            monthFooter = monthFooter,
            monthContainer = monthContainer,
            onItemPlaced = state.placementInfo::onItemPlaced
        )
    }
}