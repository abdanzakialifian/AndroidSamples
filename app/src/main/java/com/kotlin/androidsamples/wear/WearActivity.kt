package com.kotlin.androidsamples.wear

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kotlin.androidsamples.wear.finding.FindingScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WearActivity : ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                WearableGraph()
            }
        }
    }

    @Composable
    private fun WearableGraph() {
        val navController = rememberNavController()

        NavHost(navController = navController, startDestination = Screen.Finding.route) {
            composable(Screen.Finding.route) {
                FindingScreen()
            }
        }
    }
}