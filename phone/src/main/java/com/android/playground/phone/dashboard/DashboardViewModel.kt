package com.android.playground.phone.dashboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

class DashboardViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<DashboardUiState> = MutableStateFlow(DashboardUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _effects: Channel<DashboardEffect> = Channel(Channel.BUFFERED)
    val effects get() = _effects.receiveAsFlow()


    fun onIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.LoadDeviceInfo -> _uiState.update { it.copy(deviceInfo = intent.deviceInfo) }
            DashboardIntent.OnGoToBackScreen -> _effects.trySend(DashboardEffect.GoToBackScreen)
        }
    }
}