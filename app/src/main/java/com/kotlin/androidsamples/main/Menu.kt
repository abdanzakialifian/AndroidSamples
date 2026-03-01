package com.kotlin.androidsamples.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.kotlin.androidsamples.R

data class Menu(
    @field:StringRes val moduleName: Int,
    @field:DrawableRes val icon: Int,
    val targetActivityPath: String,
    val backgroundColor: Color,
    val title: String,
    val description: String
)

val menus = listOf(
    Menu(
        moduleName = R.string.module_mock_response_retrofit,
        icon = R.drawable.box,
        targetActivityPath = "com.kotlin.androidsamples.mockresponseretrofit.presentation.MockResponseRetrofitActivity",
        backgroundColor = Color(0xFFEEF2FF),
        title = "Mock Retrofit",
        description = "Api simulation & network layer"
    ),
    Menu(
        moduleName = R.string.module_dynamic_app_launcher,
        icon = R.drawable.grid,
        targetActivityPath = "com.kotlin.androidsamples.dynamicapplauncher.DynamicAppLauncherActivity",
        backgroundColor = Color(0xFFF5F3FF),
        title = "Dynamic App Launcher",
        description = "Dynamic app icon from firebase"
    ),
    Menu(
        moduleName = R.string.module_android_chart,
        icon = R.drawable.chart,
        targetActivityPath = "com.kotlin.androidsamples.androidchart.AndroidChartActivity",
        backgroundColor = Color(0xFFF0FDF4),
        title = "Android Chart",
        description = "Data visualization & graphs"
    ),
    Menu(
        moduleName = R.string.module_web_view_callback,
        icon = R.drawable.globe,
        targetActivityPath = "com.kotlin.androidsamples.webviewcallback.WebViewCallbackActivity",
        backgroundColor = Color(0xFFFFF7ED),
        title = "WebView Callback",
        description = "WebView callback for android"
    ),
    Menu(
        moduleName = R.string.module_calendar_view,
        icon = R.drawable.calendar,
        targetActivityPath = "com.kotlin.androidsamples.calendarview.screen.CalendarViewActivity",
        backgroundColor = Color(0xFFFEF2F2),
        title = "Calendar View",
        description = "Date pickers & scheduling"
    ),
    Menu(
        moduleName = R.string.module_calendar_view,
        icon = R.drawable.watch,
        targetActivityPath = "com.kotlin.androidsamples.calendarview.screen.CalendarViewActivity",
        backgroundColor = Color(0xFFF1F5F9),
        title = "WearOS",
        description = "Smartwatch UI & complication"
    )
)