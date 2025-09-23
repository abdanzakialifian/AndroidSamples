package com.kotlin.androidsamples.calendarview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kotlin.androidsamples.calendarview.navigation.NavigationStack

class CalendarViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationStack()
        }
    }
}