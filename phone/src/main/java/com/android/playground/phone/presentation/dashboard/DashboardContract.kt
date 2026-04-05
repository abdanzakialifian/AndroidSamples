package com.android.playground.phone.presentation.dashboard

import com.android.playground.device.DeviceInfo

sealed class DashboardIntent {
    data class LoadDeviceInfo(val deviceInfo: DeviceInfo) : DashboardIntent()
    data object OnGoToBackScreen : DashboardIntent()
}

data class DashboardUiState(
    val deviceInfo: DeviceInfo = DeviceInfo()
)

sealed interface DashboardEffect {
    data object GoToBackScreen : DashboardEffect
}