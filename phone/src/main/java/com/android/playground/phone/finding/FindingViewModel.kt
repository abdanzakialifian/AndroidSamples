package com.android.playground.phone.finding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playground.device.DataLayerPath
import com.google.android.gms.wearable.CapabilityClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FindingViewModel(
    private val capabilityClient: CapabilityClient,
) : ViewModel() {
    private val _uiState: MutableStateFlow<FindingUiState> = MutableStateFlow(FindingUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _effects: Channel<FindingEffect> = Channel(Channel.BUFFERED)
    val effects get() = _effects.receiveAsFlow()

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var addCapabilityChangedListenerJob: Job? = null

    val capabilityChangedListener = CapabilityClient.OnCapabilityChangedListener { capabilityInfo ->
        _uiState.update { it.copy(nodes = capabilityInfo.nodes) }
    }

    init {
        onIntent(FindingIntent.StartCountdown)
    }

    fun onIntent(intent: FindingIntent) {
        when (intent) {
            FindingIntent.AddCapabilityChangedListener -> addCapabilityChangedListener()
            FindingIntent.GetCapability -> getCapability()
            FindingIntent.RemoveCapabilityChangedListener -> removeCapabilityChangedListener()
            FindingIntent.StartCountdown -> startCountdown()
            is FindingIntent.OnNodeSelected -> _effects.trySend(
                FindingEffect.GoToDetailScreen(intent.node)
            )
        }
    }

    private fun startCountdown() {
        viewModelScope.launch {
            _uiState.update { it.copy(time = 15) }
            while (uiState.value.time > 0) {
                delay(1000L)
                _uiState.update { it.copy(time = it.time - 1) }
            }
        }
    }

    private fun getCapability() {
        viewModelScope.launch {
            runCatching {
                val capabilityInfo =
                    capabilityClient.getCapability(
                        DataLayerPath.WEAR_CAPABILITY,
                        CapabilityClient.FILTER_REACHABLE
                    ).await()
                _uiState.update { it.copy(nodes = capabilityInfo?.nodes.orEmpty()) }
            }
        }
    }

    private fun addCapabilityChangedListener() {
        addCapabilityChangedListenerJob?.start()
        addCapabilityChangedListenerJob = viewModelScope.launch {
            runCatching {
                capabilityClient.addListener(
                    capabilityChangedListener,
                    DataLayerPath.WEAR_CAPABILITY
                ).await()
            }
        }
    }

    private fun removeCapabilityChangedListener() {
        coroutineScope.launch {
            withContext(NonCancellable) {
                runCatching {
                    addCapabilityChangedListenerJob?.cancel()
                    capabilityClient.removeListener(
                        capabilityChangedListener,
                        DataLayerPath.WEAR_CAPABILITY
                    ).await()
                }
            }
        }
    }
}