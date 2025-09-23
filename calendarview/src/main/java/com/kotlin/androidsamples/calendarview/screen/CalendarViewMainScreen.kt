package com.kotlin.androidsamples.calendarview.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CalendarViewMainScreen(
    onClickCalendarViewFirst: () -> Unit,
    onClickCalendarViewSecond: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClickCalendarViewFirst) {
            Text("Calendar View First")
        }

        Button(onClickCalendarViewSecond) {
            Text("Calendar View Second")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarViewMainScreenPreview() {
    CalendarViewMainScreen(
        onClickCalendarViewFirst = {},
        onClickCalendarViewSecond = {},
    )
}