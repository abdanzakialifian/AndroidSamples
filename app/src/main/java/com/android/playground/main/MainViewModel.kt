package com.android.playground.main

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playground.R
import com.android.playground.core.common.Constants
import com.android.playground.di.DynamicFeatureLoader
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.ref.WeakReference

class MainViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _effects: Channel<MainEffect> = Channel(capacity = Channel.BUFFERED)
    val effects get() = _effects.receiveAsFlow()

    private var context: WeakReference<Context>? = null

    private val splitInstallManager by lazy {
        context?.get()?.let {
            SplitInstallManagerFactory.create(it)
        }
    }

    private val splitInstallManagerListener =
        SplitInstallStateUpdatedListener(::handleSplitInstallState)

    fun attachContext(context: Context) {
        this.context = WeakReference(context)
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

    private fun handleSplitInstallState(state: SplitInstallSessionState) {
        if (state.sessionId() != _uiState.value.sessionId) return

        when (state.status()) {
            SplitInstallSessionStatus.CANCELED -> Unit

            SplitInstallSessionStatus.CANCELING -> Unit

            SplitInstallSessionStatus.DOWNLOADED -> Unit

            SplitInstallSessionStatus.DOWNLOADING -> Unit

            SplitInstallSessionStatus.FAILED -> Unit

            SplitInstallSessionStatus.INSTALLED -> finalizeModuleLoad(_uiState.value.targetActivityPath)

            SplitInstallSessionStatus.INSTALLING -> Unit

            SplitInstallSessionStatus.PENDING -> Unit

            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> Unit

            SplitInstallSessionStatus.UNKNOWN -> Unit
        }
    }

    private fun navigateToDynamicFeatureModule(
        moduleName: String,
        targetActivityPath: String,
    ) {
        try {
            _uiState.update {
                it.copy(
                    targetActivityPath = targetActivityPath,
                    moduleName = moduleName
                )
            }
            if (splitInstallManager?.installedModules?.contains(moduleName) == true) {
                finalizeModuleLoad(targetActivityPath)
            } else {
                initSplitInstallManager(moduleName)
            }
        } catch (e: Exception) {
            Log.d(this::class.java.simpleName, "ERROR : ${e.message}")
        }
    }

    private fun initSplitInstallManager(moduleName: String) {
        viewModelScope.launch {
            splitInstallManager?.registerListener(splitInstallManagerListener)
            val request = SplitInstallRequest
                .newBuilder()
                .addModule(moduleName)
                .build()
            try {
                val sessionId = splitInstallManager?.startInstall(request)?.await()
                _uiState.update { it.copy(sessionId = sessionId ?: 0) }
            } catch (e: Exception) {
                Log.d(this::class.java.simpleName, "ERROR : ${e.message}")
            }
        }
    }

    private fun finalizeModuleLoad(targetPath: String) {
        val providerClassName = determineProviderPath(_uiState.value.moduleName)
        if (!providerClassName.isNullOrEmpty()) {
            DynamicFeatureLoader.load(providerClassName)
        }
        _effects.trySend(MainEffect.NavigateToDynamicFeatureModule(targetPath))
    }

    private fun determineProviderPath(moduleName: String): String? {
        return when (moduleName) {
            context?.get()?.getString(R.string.module_mock_okhttp) -> Constants.MOCK_OKHTTP_PROVIDER
            context?.get()?.getString(R.string.module_phone) -> Constants.MOCK_PHONE_PROVIDER
            else -> null
        }
    }

    override fun onCleared() {
        super.onCleared()
        splitInstallManager?.unregisterListener(splitInstallManagerListener)
    }
}