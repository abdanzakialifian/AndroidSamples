package com.android.playground.phone

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.android.playground.core.common.createNavType
import com.android.playground.phone.detail.DetailScreen
import com.android.playground.phone.detail.NodeUi
import com.android.playground.phone.finding.FindingScreen
import kotlin.reflect.typeOf

class PhoneActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val colorScheme = if (isSystemInDarkTheme()) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
            MaterialTheme(colorScheme = colorScheme) {
                Scaffold { paddingValues ->
                    WearableGraph(modifier = Modifier.padding(paddingValues))
                }
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
                    onNavigateBack = {
                        finish()
                    }
                )
            }
        }
    }
}