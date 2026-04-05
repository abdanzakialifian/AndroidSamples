package com.android.playground.phone.presentation.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.playground.ui.PlaygroundTheme
import org.koin.compose.viewmodel.koinViewModel
import com.android.playground.device.DeviceInfo

@Composable
fun DashboardScreen(
    deviceInfo: DeviceInfo,
    viewModel: DashboardViewModel = koinViewModel(),
    onGoToBackScreen: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                DashboardEffect.GoToBackScreen -> onGoToBackScreen()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(DashboardIntent.LoadDeviceInfo(deviceInfo))
    }

    BackHandler {
        viewModel.onIntent(DashboardIntent.OnGoToBackScreen)
    }

    DashboardContent(
        uiState = uiState
    )
}

@Composable
private fun DashboardContent(
    uiState: DashboardUiState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Watch Information",
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = buildString {
                append("Manufacturer : ")
                append(uiState.deviceInfo.manufacturer)
            },
        )

        Text(
            text = buildString {
                append("Model : ")
                append(uiState.deviceInfo.model)
            },
        )

        Text(
            text = buildString {
                append("Brand : ")
                append(uiState.deviceInfo.brand)
            },
        )

        Text(
            text = buildString {
                append("Device : ")
                append(uiState.deviceInfo.device)
            },
        )

        Text(
            text = buildString {
                append("Product : ")
                append(uiState.deviceInfo.product)
            },
        )

        Text(
            text = buildString {
                append("SDK Version : ")
                append(uiState.deviceInfo.sdkVersion)
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardContentPreview() {
    PlaygroundTheme {
        DashboardContent(
            uiState = DashboardUiState()
        )
    }
}