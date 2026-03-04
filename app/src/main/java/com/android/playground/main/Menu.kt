package com.android.playground.main

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.android.playground.R

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
        moduleName = R.string.module_mock_okhttp,
        icon = R.drawable.box,
        targetActivityPath = "com.android.playground.mockokhttp.presentation.MockOkhttpActivity",
        backgroundColor = Color(0xFFEEF2FF),
        title = "Mock Okhttp",
        description = "Api simulation & network layer"
    ),
    Menu(
        moduleName = R.string.module_app_launcher,
        icon = R.drawable.grid,
        targetActivityPath = "com.android.playground.applauncher.AppLauncherActivity",
        backgroundColor = Color(0xFFF5F3FF),
        title = "App Launcher",
        description = "Dynamic app icon from firebase"
    ),
    Menu(
        moduleName = R.string.module_chart,
        icon = R.drawable.chart,
        targetActivityPath = "com.android.playground.chart.ChartActivity",
        backgroundColor = Color(0xFFF0FDF4),
        title = "Chart",
        description = "Data visualization & graphs"
    ),
    Menu(
        moduleName = R.string.module_web_view,
        icon = R.drawable.globe,
        targetActivityPath = "com.android.playground.webview.WebViewCallbackActivity",
        backgroundColor = Color(0xFFFFF7ED),
        title = "WebView",
        description = "WebView callback for android"
    ),
    Menu(
        moduleName = R.string.module_calendar,
        icon = R.drawable.calendar,
        targetActivityPath = "com.android.playground.calendar.screen.CalendarViewActivity",
        backgroundColor = Color(0xFFFEF2F2),
        title = "Calendar",
        description = "Date pickers & scheduling"
    ),
    Menu(
        moduleName = R.string.module_phone,
        icon = R.drawable.watch,
        targetActivityPath = "com.android.playground.phone.PhoneActivity",
        backgroundColor = Color(0xFFF1F5F9),
        title = "WearOS",
        description = "Smartwatch UI & complication"
    )
)