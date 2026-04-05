package com.android.playground.phone.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.android.playground.core.common.createNavType
import com.android.playground.device.DeviceInfo
import com.android.playground.phone.presentation.dashboard.DashboardScreen
import com.android.playground.phone.presentation.detail.DetailScreen
import com.android.playground.phone.presentation.detail.NodeUi
import com.android.playground.phone.presentation.finding.FindingScreen
import com.android.playground.ui.PlaygroundTheme
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

class PhoneActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlaygroundTheme {
                WearableGraph()
            }
        }
    }

    @Composable
    private fun WearableGraph(modifier: Modifier = Modifier) {
        val navController = rememberNavController()
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = Screen.Finding
        ) {
            composable<Screen.Finding> {
                FindingScreen(
                    onGoToDetailScreen = { node ->
                        val nodeUi = NodeUi(
                            id = node.id,
                            displayName = node.displayName,
                            isNearby = node.isNearby
                        )
                        navController.navigate(Screen.Detail(nodeUi))
                    }
                )
            }

            composable<Screen.Detail>(
                typeMap = mapOf(
                    typeOf<NodeUi>() to createNavType(NodeUi.serializer())
                )
            ) { backStackEntry ->
                val detail = backStackEntry.toRoute<Screen.Detail>()
                DetailScreen(
                    node = detail.node,
                    onGoToDashboardScreen = { deviceInfoJson ->
                        navController.navigate(Screen.Dashboard(deviceInfoJson))
                    },
                    onGoToBackScreen = {
                        finish()
                    }
                )
            }

            composable<Screen.Dashboard> { backStackEntry ->
                val dashboard = backStackEntry.toRoute<Screen.Dashboard>()
                val deviceInfo = Json.decodeFromString<DeviceInfo>(dashboard.deviceInfoJson)
                DashboardScreen(
                    deviceInfo = deviceInfo,
                    onGoToBackScreen = {
                        finish()
                    }
                )
            }
        }
    }
}