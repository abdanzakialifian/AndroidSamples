package com.kotlin.androidsamples.wear.finding

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.delay
import kotlinx.coroutines.tasks.await
import java.util.Locale

@Composable
fun FindingScreen(viewModel: FindingViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var time by remember { mutableIntStateOf(15) }

    val context = LocalContext.current

    val capabilityClient = Wearable.getCapabilityClient(context)

    val capabilityChangedListener = remember {
        CapabilityClient.OnCapabilityChangedListener { capabilityInfo ->
            Log.d("CEK", "NODES : ${capabilityInfo.nodes}")
            Log.d("CEK", "NAME : ${capabilityInfo.name}")
            viewModel.setNodes(capabilityInfo.nodes)
        }
    }

    LaunchedEffect(Unit) {
        runCatching {
            capabilityClient.getCapability("wear", CapabilityClient.FILTER_REACHABLE).await()
        }.onSuccess {
            viewModel.setNodes(it.nodes)
        }.onFailure {
            Log.d("CEK", "FAILURE : $it")
        }
    }

    LaunchedEffect(Unit) {
        while (time > 0) {
            delay(1000L)
            time--
        }
    }

    DisposableEffect(time > 0) {
        if (time > 0) {
            capabilityClient.addListener(capabilityChangedListener, "wear")
        }
        onDispose {
            capabilityClient.removeListener(capabilityChangedListener, "wear")
        }
    }

    FindingContent(
        uiState = uiState,
        time = time,
    )
}

@Composable
private fun FindingContent(
    uiState: FindingUiState,
    time: Int,
) {
    val minutes = time / 60
    val seconds = time % 60

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds),
                color = MaterialTheme.colorScheme.onPrimary
            )

            LazyColumn {
                items(items = uiState.nodes.toList()) {
                    Text(
                        text = buildString {
                            append(it.id)
                            append(" -- ")
                            append(it.isNearby)
                            append(" -- ")
                            append(it.displayName)
                        },
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, apiLevel = 35)
@Composable
private fun FindingContentPreview() {
    val context = LocalContext.current
    val colorScheme =
        if (isSystemInDarkTheme()) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
            context
        )
    MaterialTheme(colorScheme = colorScheme) {
        FindingContent(
            uiState = FindingUiState(),
            time = 15,
        )
    }
}