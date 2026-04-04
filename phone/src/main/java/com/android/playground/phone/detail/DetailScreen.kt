package com.android.playground.phone.detail

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(
    node: NodeUi,
    viewModel: DetailViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.onIntent(DetailIntent.SetNodeOfWatch(node))
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                DetailEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    BackHandler {
        viewModel.onIntent(DetailIntent.OnNavigateBack)
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                modifier = Modifier.weight(1F),
                onClick = {
                    onIntent(DetailIntent.OnNavigateBack)
                },
                content = {
                    Text(text = "Back")
                }
            )

            Button(
                modifier = Modifier.weight(1F),
                onClick = {
                    onIntent(DetailIntent.RequestConnect(uiState.nodeId))
                },
                content = {
                    Text(text = "Connect")
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    val context = LocalContext.current
    val colorScheme =
        if (isSystemInDarkTheme()) dynamicDarkColorScheme(context) else dynamicLightColorScheme(
            context
        )
    MaterialTheme(colorScheme = colorScheme) {
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