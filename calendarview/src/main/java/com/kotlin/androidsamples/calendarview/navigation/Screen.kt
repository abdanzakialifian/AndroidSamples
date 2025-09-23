package com.kotlin.androidsamples.calendarview.navigation

sealed class Screen(val route: String) {
    data object CalendarViewMain : Screen("main_screen")
    data object CalendarViewFirst : Screen("first_screen")
    data object CalendarViewSecond : Screen("second_screen")
}