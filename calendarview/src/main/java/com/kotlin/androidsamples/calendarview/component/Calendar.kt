package com.kotlin.androidsamples.calendarview.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotlin.androidsamples.calendarview.model.CalendarDay
import com.kotlin.androidsamples.calendarview.CalendarState
import com.kotlin.androidsamples.calendarview.DayPosition
import com.kotlin.androidsamples.calendarview.Utils
import com.kotlin.androidsamples.calendarview.rememberCalendarState
import com.kotlin.androidsamples.calendarview.ui.theme.Black5
import com.kotlin.androidsamples.calendarview.ui.theme.Grey2
import com.kotlin.androidsamples.calendarview.ui.theme.Red2
import com.kotlin.androidsamples.calendarview.ui.theme.SoftPeach
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun Calendar(
    modifier: Modifier = Modifier,
    state: CalendarState,
    dayOfWeeks: List<DayOfWeek>,
    selectedDate: LocalDate?,
    onDateClicked: (LocalDate) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        state = state.listState,
        flingBehavior = pagedFlingBehavior(state.listState),
        userScrollEnabled = true,
    ) {
        items(
            count = state.calendarInfo.indexCount.toInt(),
            key = { offset -> state.store[offset].yearMonth }
        ) { offset ->
            val month = state.store[offset]
            Column(modifier = Modifier.fillParentMaxWidth()) {
                Day(dayOfWeeks)

                Column(modifier = Modifier.fillMaxWidth()) {
                    month.weekDays.forEachIndexed { row, week ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            week.forEachIndexed { column, day ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(4.dp)
                                ) {
                                    if (day.position == DayPosition.MonthDate) {
                                        Date(
                                            day = day,
                                            isSelectedDate = selectedDate == day.date,
                                            onClick = onDateClicked,
                                        )
                                    }
                                }
                            }
                        }
                        HorizontalDivider(color = Grey2)
                    }
                }
            }
        }
    }
}

@Composable
fun Day(dayOfWeeks: List<DayOfWeek>, modifier: Modifier = Modifier) {
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
private fun Date(
    day: CalendarDay,
    isSelectedDate: Boolean,
    onClick: (LocalDate) -> Unit,
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .background(
                color = if (isSelectedDate) SoftPeach else Color.Transparent,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(4.dp)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = {
                    onClick(day.date)
                }
            )
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 3.dp, end = 4.dp),
            text = day.date.dayOfMonth.toString(),
            color = if (day.date.dayOfWeek == DayOfWeek.SUNDAY) Red2 else Black5,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun pagedFlingBehavior(state: LazyListState): FlingBehavior {
    val snappingLayout = remember(state) {
        val provider = SnapLayoutInfoProvider(state, SnapPosition.Start)
        object : SnapLayoutInfoProvider by provider {
            override fun calculateApproachOffset(velocity: Float, decayOffset: Float): Float = 0F
        }
    }
    return rememberSnapFlingBehavior(snappingLayout)
}

@Preview(showBackground = true)
@Composable
private fun CalendarPreview() {
    Calendar(
        state = rememberCalendarState(),
        dayOfWeeks = Utils.daysOfWeek(),
        selectedDate = null,
        onDateClicked = {}
    )
}