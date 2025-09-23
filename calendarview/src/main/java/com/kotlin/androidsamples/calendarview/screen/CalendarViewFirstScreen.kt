package com.kotlin.androidsamples.calendarview.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun CalendarViewFirstScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Calendar View First Screen")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CalendarViewFirstScreenPreview() {
    CalendarViewFirstScreen()
}