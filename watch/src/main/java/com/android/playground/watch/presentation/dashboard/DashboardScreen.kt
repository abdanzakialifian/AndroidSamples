package com.android.playground.watch.presentation.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.TransformingLazyColumn
import androidx.wear.compose.foundation.lazy.rememberTransformingLazyColumnState
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.MaterialTheme
import androidx.wear.compose.material3.ScreenScaffold
import androidx.wear.compose.material3.SurfaceTransformation
import androidx.wear.compose.material3.lazy.rememberTransformationSpec
import androidx.wear.compose.material3.lazy.transformedHeight
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.android.playground.device.DeviceInfo
import com.android.playground.watch.presentation.ui.WatchTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun DashboardScreen(
    deviceInfo: DeviceInfo,
    viewModel: DashboardViewModel = koinViewModel(),
    onGoToBackScreen: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when(effect) {
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

    DashboardContent(uiState)
}

@Composable
private fun DashboardContent(
    uiState: DashboardUiState
) {
    val columnState = rememberTransformingLazyColumnState()
    val transformationSpec = rememberTransformationSpec()
    ScreenScaffold(scrollState = columnState) { contentPadding ->
        TransformingLazyColumn(
            state = columnState,
            contentPadding = PaddingValues(
                start = contentPadding.calculateStartPadding(LayoutDirection.Ltr),
                top = contentPadding.calculateTopPadding(),
                end = contentPadding.calculateEndPadding(LayoutDirection.Ltr),
                bottom = contentPadding.calculateBottomPadding() * 3
            )
        ) {
            item {
                ListHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .transformedHeight(this, transformationSpec),
                    transformation = SurfaceTransformation(transformationSpec)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Phone Information",
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .paint(
                            painter = SurfaceTransformation(transformationSpec).createContainerPainter(
                                painter = ColorPainter(Color.Transparent),
                                shape = RoundedCornerShape(Int.MAX_VALUE.dp)
                            )
                        )
                        .graphicsLayer {
                            with(SurfaceTransformation(transformationSpec)) { applyContainerTransformation() }
                        }
                        .transformedHeight(this, transformationSpec),
                ) {
                    Text(
                        text = buildString {
                            append("Manufacturer : ")
                            append(uiState.deviceInfo.manufacturer)
                        },
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = buildString {
                            append("Model : ")
                            append(uiState.deviceInfo.model)
                        },
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = buildString {
                            append("Brand : ")
                            append(uiState.deviceInfo.brand)
                        },
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = buildString {
                            append("Device : ")
                            append(uiState.deviceInfo.device)
                        },
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = buildString {
                            append("Product : ")
                            append(uiState.deviceInfo.product)
                        },
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = buildString {
                            append("SDK Version : ")
                            append(uiState.deviceInfo.sdkVersion)
                        },
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@WearPreviewDevices
@Composable
private fun DashboardContentPreview() {
    WatchTheme {
        DashboardContent(
            uiState = DashboardUiState()
        )
    }
}