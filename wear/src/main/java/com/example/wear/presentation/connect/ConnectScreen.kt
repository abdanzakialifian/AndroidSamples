package com.example.wear.presentation.connect

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.example.wear.presentation.theme.AndroidSamplesTheme
import kotlinx.coroutines.awaitCancellation

@Composable
fun ConnectScreen(viewModel: ConnectViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.addLocalCapability()

        try {
            awaitCancellation()
        } finally {
            viewModel.removeLocalCapability()
        }
    }

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
private fun ConnectScreenPreview() {
    AndroidSamplesTheme {
        ConnectScreen()
    }
}