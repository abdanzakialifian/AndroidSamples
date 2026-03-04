package com.android.playground.calendar.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.playground.calendar.screen.CalendarViewFirstScreen
import com.android.playground.calendar.screen.CalendarViewMainScreen
import com.android.playground.calendar.screen.CalendarViewSecondScreen

@Composable
fun NavigationStack() {
    Scaffold { innerPadding ->
        val navController = rememberNavController()
        NavHost(modifier = Modifier.padding(innerPadding), navController = navController, startDestination = Screen.CalendarViewMain.route) {
            composable(route = Screen.CalendarViewMain.route) {
                CalendarViewMainScreen(
                    onClickCalendarViewFirst = {
                        navController.navigate(Screen.CalendarViewFirst.route)
                    },
                    onClickCalendarViewSecond = {
                        navController.navigate(Screen.CalendarViewSecond.route)
                    }
                )
            }
            composable(route = Screen.CalendarViewFirst.route) {
                CalendarViewFirstScreen()
            }
            composable(route = Screen.CalendarViewSecond.route) {
                CalendarViewSecondScreen()
            }
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4, showSystemUi = true)
@Composable
fun NavigationStackPreview() {
    NavigationStack()
}