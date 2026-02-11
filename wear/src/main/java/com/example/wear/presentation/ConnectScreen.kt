package com.example.wear.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.CompactButton
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.example.wear.presentation.theme.AndroidSamplesTheme
import com.google.android.gms.wearable.Wearable

@Composable
fun ConnectScreen(
    onCancel: () -> Unit,
) {
    val context = LocalContext.current

    val capabilityClient = Wearable.getCapabilityClient(context)

    LaunchedEffect(Unit) {
        capabilityClient.addLocalCapability("wear")
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

            CompactButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    capabilityClient.removeLocalCapability("wear")
                    onCancel()
                },
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Cancel",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            )
        }
    }
}

@WearPreviewDevices
@Composable
private fun ConnectScreenPreview() {
    AndroidSamplesTheme {
        ConnectScreen(
            onCancel = {}
        )
    }
}