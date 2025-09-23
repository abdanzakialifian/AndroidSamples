package com.kotlin.androidsamples.calendarview.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kotlin.androidsamples.calendarview.CalendarDay
import com.kotlin.androidsamples.calendarview.OutDateStyle
import com.kotlin.androidsamples.calendarview.Utils
import com.kotlin.androidsamples.calendarview.Utils.nextMonth
import com.kotlin.androidsamples.calendarview.Utils.previousMonth
import com.kotlin.androidsamples.calendarview.component.SimpleCalendarTitle
import com.kotlin.androidsamples.calendarview.rememberCalendarState
import com.kotlin.androidsamples.calendarview.rememberFirstCompletelyVisibleMonth
import kotlinx.coroutines.launch
import java.time.YearMonth

@Composable
fun CalendarViewFirstScreen() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var selection by remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { Utils.daysOfWeek() }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val state = rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstDayOfWeek = daysOfWeek.first(),
            firstVisibleMonth = currentMonth,
            outDateStyle = OutDateStyle.EndOfGrid
        )
        val coroutineScope = rememberCoroutineScope()
        val visibleMonth = rememberFirstCompletelyVisibleMonth(state)
        LaunchedEffect(visibleMonth) {
            selection = null
        }

        CompositionLocalProvider(LocalContentColor provides Color.White) {
            SimpleCalendarTitle(
                modifier = Modifier
                    .background(Color.DarkGray)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                currentMonth = visibleMonth.yearMonth,
                goToPrevious = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                    }
                },
                goToNext = {
                    coroutineScope.launch {
                        state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarViewFirstScreenPreview() {
    CalendarViewFirstScreen()
}