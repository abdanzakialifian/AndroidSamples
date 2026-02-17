package com.kotlin.androidsamples.wear.finding

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.wearable.Node
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun FindingScreen(
    viewModel: FindingViewModel = hiltViewModel(),
    onClick: (Node) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.startCountdown()
    }

    LaunchedEffect(uiState.time > 0) {
        if (uiState.time == 0) {
            return@LaunchedEffect
        }

        viewModel.getCapability()
        viewModel.addListener()

        try {
            awaitCancellation()
        } finally {
            viewModel.removeListener()
        }
    }

    FindingContent(
        uiState = uiState,
        onClick = onClick,
        onRetry = {
            scope.launch {
                viewModel.startCountdown()
            }
        },
    )
}

@Composable
private fun FindingContent(
    uiState: FindingUiState,
    onClick: (Node) -> Unit,
    onRetry: () -> Unit,
) {
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
            )

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items = uiState.nodes.toList()) { node ->
                    Text(
                        modifier = Modifier
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    onClick(node)
                                }
                            )
                            .padding(4.dp),
                        text = node.displayName,
                    )
                }

                item {
                    if (uiState.nodes.isEmpty() && uiState.time == 0) {
                        Button(
                            modifier = Modifier.padding(top = 16.dp),
                            onClick = onRetry,
                            content = {
                                Text(text = "Search Again")
                            }
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
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
            onClick = {},
            onRetry = {}
        )
    }
}