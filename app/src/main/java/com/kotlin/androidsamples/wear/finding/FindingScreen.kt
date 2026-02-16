package com.kotlin.androidsamples.wear.finding

import android.os.Build
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun FindingScreen(viewModel: FindingViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.getCapability()
    }

    DisposableEffect(uiState.time > 0) {
        if (uiState.time > 0) {
            scope.launch {
                viewModel.addListener()
            }
        }
        onDispose {
            scope.launch {
                viewModel.removeListener()
            }
        }
    }

    FindingContent(uiState = uiState)
}

@Composable
private fun FindingContent(uiState: FindingUiState) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = String.format(
                    Locale.getDefault(),
                    "%02d:%02d",
                    uiState.minutes,
                    uiState.seconds
                ),
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
        )
    }
}