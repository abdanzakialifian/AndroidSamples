package com.android.playground.phone.presentation.detail

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.playground.ui.PlaygroundTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    node: NodeUi,
    viewModel: DetailViewModel = koinViewModel(),
    onGoToDashboardScreen: (String) -> Unit,
    onGoToBackScreen: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(DetailIntent.LoadNode(node))
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is DetailEffect.GoToDashboardScreen -> onGoToDashboardScreen(effect.deviceInfoJson)
                DetailEffect.GoToBackScreen -> onGoToBackScreen()
            }
        }
    }

    BackHandler {
        viewModel.onIntent(DetailIntent.OnGoToBackScreen)
    }

    DetailContent(
        uiState = uiState,
        onIntent = viewModel::onIntent
    )
}

@Composable
private fun DetailContent(
    uiState: DetailUiState,
    onIntent: (DetailIntent) -> Unit
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
                text = buildString {
                    append("Id : ")
                    append(uiState.nodeId)
                },
            )

            Text(
                text = buildString {
                    append("Name : ")
                    append(uiState.displayName)
                },
            )

            Text(
                text = buildString {
                    append("Nearby : ")
                    append(uiState.isNearby)
                },
            )

            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    onIntent(DetailIntent.RequestConnect(uiState.nodeId))
                },
                content = {
                    Text(text = "Connect")
                }
            )
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    PlaygroundTheme {
        DetailContent(
            uiState = DetailUiState(
                nodeId = "12345",
                displayName = "Galaxy Wearable",
                isNearby = true,
            ),
            onIntent = {}
        )
    }
}