package com.android.playground.calendar.screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.android.playground.calendar.navigation.NavigationStack
import com.android.playground.ui.PlaygroundTheme

class CalendarViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaygroundTheme {
                NavigationStack()
            }
        }
    }
}