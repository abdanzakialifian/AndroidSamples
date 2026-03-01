package com.kotlin.androidsamples.main

data class MainUiState(
    val sessionId: Int = 0,
    val targetActivityPath: String = "",
)

sealed interface MainIntent {
    data class NavigateDynamicFeatureModule(
        val moduleName: String,
        val targetActivityPath: String,
    ) : MainIntent
}

sealed interface MainEffect {
    data class NavigateToDynamicFeatureModule(
        val targetActivityPath: String
    ) : MainEffect
}