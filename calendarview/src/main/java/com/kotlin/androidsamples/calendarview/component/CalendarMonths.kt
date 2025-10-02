package com.kotlin.androidsamples.calendarview.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.kotlin.androidsamples.calendarview.CalendarDay
import com.kotlin.androidsamples.calendarview.CalendarMonth
import com.kotlin.androidsamples.calendarview.ContentHeightMode
import com.kotlin.androidsamples.calendarview.ui.theme.Grey2

internal fun LazyListScope.CalendarMonths(
    monthCount: Int,
    contentHeightMode: ContentHeightMode,
    monthData: (offset: Int) -> CalendarMonth,
    dayContent: @Composable BoxScope.(CalendarDay) -> Unit,
    monthHeader: (@Composable ColumnScope.(CalendarMonth) -> Unit)?,
    monthBody: (@Composable ColumnScope.(CalendarMonth, content: @Composable () -> Unit) -> Unit)?,
    monthFooter: (@Composable ColumnScope.(CalendarMonth) -> Unit)?,
    monthContainer: (@Composable LazyItemScope.(CalendarMonth, container: @Composable () -> Unit) -> Unit)?,
) {
    items(
        count = monthCount,
        key = { offset ->
            monthData(offset).yearMonth
        }
    ) { offset ->
        val month = monthData(offset)
        val fillHeight = when (contentHeightMode) {
            ContentHeightMode.WRAP -> false
            ContentHeightMode.FILL -> true
        }
        val hasMonthContainer = monthContainer != null
        Box {
            monthContainer.or(defaultMonthContainer)(month) {
                Column(
                    modifier = Modifier
                        .then(if (hasMonthContainer) Modifier.fillMaxWidth() else Modifier.fillParentMaxWidth())
                        .then(
                            if (fillHeight) {
                                if (hasMonthContainer) {
                                    Modifier.fillMaxHeight()
                                } else {
                                    Modifier.fillParentMaxHeight()
                                }
                            } else {
                                Modifier.wrapContentHeight()
                            }
                        )
                ) {
                    monthHeader?.invoke(this, month)
                    monthBody.or(defaultMonthBody)(month) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .then(if (fillHeight) Modifier.weight(1f) else Modifier.wrapContentHeight())
                        ) {
                            month.weekDays.forEachIndexed { row, week ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .then(if (fillHeight) Modifier.weight(1f) else Modifier.wrapContentHeight())
                                ) {
                                    week.forEachIndexed { column, day ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(if (row > 0) 4.dp else 0.dp)
                                                .clipToBounds()
                                        ) {
                                            dayContent(day)
                                        }
                                    }
                                }
                                HorizontalDivider(color = Grey2)
                            }
                        }
                    }
                    monthFooter?.invoke(this, month)
                }
            }
        }
    }
}

private val defaultMonthContainer: @Composable LazyItemScope.(CalendarMonth, container: @Composable () -> Unit) -> Unit =
    { _, container -> container() }

private val defaultMonthBody: @Composable ColumnScope.(CalendarMonth, content: @Composable () -> Unit) -> Unit =
    { _, content -> content() }

internal fun <T> T?.or(default: T): T = this ?: default