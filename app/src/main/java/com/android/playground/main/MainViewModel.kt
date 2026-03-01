package com.android.playground.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ViewModel() {
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _effects: Channel<MainEffect> = Channel(capacity = Channel.BUFFERED)
    val effects get() = _effects.receiveAsFlow()

    private val splitInstallManager by lazy { SplitInstallManagerFactory.create(context) }

    private val splitInstallManagerListener = SplitInstallStateUpdatedListener { state ->
        Log.d(this::class.java.simpleName, "State Update : $state")
        if (state.sessionId() == _uiState.value.sessionId) {
            when(state.status()) {
                SplitInstallSessionStatus.CANCELED -> {}

                SplitInstallSessionStatus.CANCELING -> {}

                SplitInstallSessionStatus.DOWNLOADED -> {}

                SplitInstallSessionStatus.DOWNLOADING -> {}

                SplitInstallSessionStatus.FAILED -> {}

                SplitInstallSessionStatus.INSTALLED -> {
                    _effects.trySend(MainEffect.NavigateToDynamicFeatureModule(_uiState.value.targetActivityPath))
                }

                SplitInstallSessionStatus.INSTALLING -> {}

                SplitInstallSessionStatus.PENDING -> {}

                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {}

                SplitInstallSessionStatus.UNKNOWN -> {}
            }
        }
    }

    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.NavigateDynamicFeatureModule -> {
                navigateToDynamicFeatureModule(
                    moduleName = intent.moduleName,
                    targetActivityPath = intent.targetActivityPath
                )
            }
        }
    }

    private fun navigateToDynamicFeatureModule(
        moduleName: String,
        targetActivityPath: String,
    ) {
        try {
            _uiState.update { it.copy(targetActivityPath = targetActivityPath) }
            if (splitInstallManager.installedModules.contains(moduleName)) {
                _effects.trySend(MainEffect.NavigateToDynamicFeatureModule(targetActivityPath))
            } else {
                initSplitInstallManager(moduleName)
            }
        } catch (e: Exception) {
            Log.d(this::class.java.simpleName, "ERROR : ${e.message}")
        }
    }

    private fun initSplitInstallManager(moduleName: String) {
        viewModelScope.launch {
            splitInstallManager.registerListener(splitInstallManagerListener)
            val request = SplitInstallRequest
                .newBuilder()
                .addModule(moduleName)
                .build()
            try {
                val sessionId = splitInstallManager.startInstall(request).await()
                _uiState.update { it.copy(sessionId = sessionId) }
            } catch (e: Exception) {
                Log.d(this::class.java.simpleName, "ERROR : ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        splitInstallManager.unregisterListener(splitInstallManagerListener)
    }
}