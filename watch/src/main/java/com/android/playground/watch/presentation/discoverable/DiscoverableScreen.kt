package com.android.playground.watch.presentation.discoverable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.android.playground.watch.presentation.ui.WatchTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun DiscoverableScreen(
    viewModel: DiscoverableViewModel = koinViewModel(),
    onGoToDashboardScreen: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is DiscoverableEffect.GoToDashboardScreen -> onGoToDashboardScreen(effect.deviceInfoJson)
            }
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onIntent(DiscoverableIntent.AddLocalCapability)
        viewModel.onIntent(DiscoverableIntent.AddMessageReceivedListener)
        viewModel.onIntent(DiscoverableIntent.AddRpcServiceListener)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        viewModel.onIntent(DiscoverableIntent.RemoveLocalCapability)
        viewModel.onIntent(DiscoverableIntent.RemoveMessageReceivedListener)
        viewModel.onIntent(DiscoverableIntent.RemoveRpcServiceListener)
    }

    DiscoverableContent(
        uiState = uiState
    )
}

@Composable
private fun DiscoverableContent(
    uiState: DiscoverableUiState
) {
    ScreenScaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(40.dp),
                strokeWidth = 6.dp
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Waiting Discoverable....",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@WearPreviewDevices
@Composable
private fun DiscoverableContentPreview() {
    WatchTheme {
        DiscoverableContent(
            uiState = DiscoverableUiState()
        )
    }
}