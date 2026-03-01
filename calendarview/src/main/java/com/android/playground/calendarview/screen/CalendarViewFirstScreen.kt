package com.android.playground.calendarview.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.playground.calendarview.enum.OutDateStyle
import com.android.playground.calendarview.Utils
import com.android.playground.calendarview.component.Calendar
import com.android.playground.calendarview.component.CalendarTitle
import com.android.playground.calendarview.extensions.completelyVisibleMonth
import com.android.playground.calendarview.extensions.nextMonth
import com.android.playground.calendarview.extensions.previousMonth
import com.android.playground.calendarview.rememberCalendarState
import com.android.playground.calendarview.ui.theme.White
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarViewFirstScreen() {
    val currentMonth = remember { YearMonth.now() }

    val startMonth = remember { currentMonth.minusMonths(500) }

    val endMonth = remember { currentMonth.plusMonths(500) }

    val daysOfWeek = remember { Utils.daysOfWeek() }

    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val coroutineScope = rememberCoroutineScope()

    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstDayOfWeek = daysOfWeek.first(),
        firstVisibleMonth = currentMonth,
        outDateStyle = OutDateStyle.EndOfRow
    )

    var visibleMonth by remember { mutableStateOf(state.firstVisibleMonth) }

    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.completelyVisibleMonth.firstOrNull() }
            .filterNotNull()
            .collect { month ->
                visibleMonth = month
                selectedDate = null
            }
    }

    Column(modifier = Modifier.background(White)) {
        CalendarTitle(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            currentMonth = visibleMonth.yearMonth,
            goToPreviousMonth = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            },
            goToNextMonth = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                }
            }
        )

        Calendar(
            state = state,
            dayOfWeeks = daysOfWeek,
            selectedDate = selectedDate,
            onDateClicked = { date ->
                selectedDate = date
            }
        )
    }
}

@Preview
@Composable
fun CalendarViewFirstScreenPreview() {
    CalendarViewFirstScreen()
}