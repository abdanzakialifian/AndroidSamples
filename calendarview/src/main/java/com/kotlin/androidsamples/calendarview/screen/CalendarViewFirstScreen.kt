package com.kotlin.androidsamples.calendarview.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.androidsamples.calendarview.CalendarDay
import com.kotlin.androidsamples.calendarview.DayPosition
import com.kotlin.androidsamples.calendarview.OutDateStyle
import com.kotlin.androidsamples.calendarview.Utils
import com.kotlin.androidsamples.calendarview.Utils.nextMonth
import com.kotlin.androidsamples.calendarview.Utils.previousMonth
import com.kotlin.androidsamples.calendarview.component.Calendar
import com.kotlin.androidsamples.calendarview.component.SimpleCalendarTitle
import com.kotlin.androidsamples.calendarview.rememberCalendarState
import com.kotlin.androidsamples.calendarview.rememberFirstCompletelyVisibleMonth
import com.kotlin.androidsamples.calendarview.ui.theme.Black5
import com.kotlin.androidsamples.calendarview.ui.theme.Red2
import com.kotlin.androidsamples.calendarview.ui.theme.SoftPeach
import com.kotlin.androidsamples.calendarview.ui.theme.White
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarViewFirstScreen() {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(500) }
    val endMonth = remember { currentMonth.plusMonths(500) }
    var daySelection by remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { Utils.daysOfWeek() }

    Column(modifier = Modifier.background(White)) {
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
            daySelection = null
        }

        SimpleCalendarTitle(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
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
        Calendar(
            state = state,
            dayContent = { day ->
                if (day.position == DayPosition.MonthDate) {
                    Day(
                        day = day,
                        isSelected = daySelection == day,
                        colors = emptyList(),
                        onClick = { day ->
                            daySelection = day
                        }
                    )
                }
            },
            monthHeader = {
                MonthHeader(
                    modifier = Modifier.padding(vertical = 8.dp),
                    dayOfWeeks = daysOfWeek
                )
            }
        )
    }
}

@Composable
fun MonthHeader(modifier: Modifier = Modifier, dayOfWeeks: List<DayOfWeek> = emptyList()) {
    Row(modifier = modifier.fillMaxWidth()) {
        for (dayOfWeek in dayOfWeeks) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = if (dayOfWeek == DayOfWeek.SUNDAY) Red2 else Black5,
                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()).uppercase(),
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
private fun Day(
    day: CalendarDay,
    isSelected: Boolean = false,
    colors: List<Color> = emptyList(),
    onClick: (CalendarDay) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(if (isSelected) SoftPeach else Color.Transparent, shape = RoundedCornerShape(4.dp))
            .padding(4.dp)
            .clickable(enabled = day.position == DayPosition.MonthDate, onClick = { onClick(day) })

    ) {
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 3.dp, end = 4.dp),
            text = day.date.dayOfMonth.toString(),
            color = if (day.date.dayOfWeek == DayOfWeek.SUNDAY) Red2 else Black5,
            fontSize = 12.sp,
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (color in colors) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp)
                        .background(color)
                )
            }
        }
    }
}

@Preview
@Composable
fun CalendarViewFirstScreenPreview() {
    CalendarViewFirstScreen()
}