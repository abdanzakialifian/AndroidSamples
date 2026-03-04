package com.android.playground.watch.presentation.connect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.android.playground.watch.presentation.theme.AndroidSamplesTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun ConnectScreen(viewModel: ConnectViewModel = koinViewModel()) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.attachContext(context)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.onIntent(ConnectIntent.AddLocalCapability)
        viewModel.onIntent(ConnectIntent.AddMessageReceivedListener)
    }

    LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
        viewModel.onIntent(ConnectIntent.RemoveLocalCapability)
        viewModel.onIntent(ConnectIntent.RemoveMessageReceivedListener)
    }

    ConnectContent()
}

@Composable
private fun ConnectContent() {
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
                text = "Waiting....",
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@WearPreviewDevices
@Composable
private fun ConnectContentPreview() {
    AndroidSamplesTheme {
        ConnectContent()
    }
}