/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter to find the
 * most up to date changes to the libraries and their usages.
 */

package com.android.playground.watch.presentation

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material3.AppScaffold
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.android.playground.watch.presentation.connect.ConnectScreen
import com.android.playground.watch.presentation.landing.LandingScreen
import com.android.playground.watch.presentation.theme.AndroidSamplesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setTheme(R.style.Theme_DeviceDefault)

        setContent {
            AndroidSamplesTheme {
                AppScaffold {
                    WearableGraph()
                }
            }
        }
    }

    @Composable
    private fun WearableGraph() {
        val navController = rememberSwipeDismissableNavController()

        SwipeDismissableNavHost(
            navController = navController,
            startDestination = Screen.Landing.route
        ) {
            composable(Screen.Landing.route) {
                LandingScreen(
                    onConnect = {
                        navController.navigate(Screen.Connect.route)
                    }
                )
            }

            composable(Screen.Connect.route) {
                ConnectScreen()
            }
        }
    }
}