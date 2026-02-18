package com.kotlin.androidsamples.wear.finding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FindingViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {
    private val _uiState: MutableStateFlow<FindingUiState> = MutableStateFlow(FindingUiState())
    val uiState: StateFlow<FindingUiState> = _uiState

    val capabilityClient = Wearable.getCapabilityClient(context)

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
                    capabilityClient.getCapability("wear", CapabilityClient.FILTER_REACHABLE).await()
                _uiState.update { it.copy(nodes = capabilityInfo.nodes) }
            }
        }
    }

    private fun addCapabilityChangedListener() {
        addCapabilityChangedListenerJob?.start()
        addCapabilityChangedListenerJob = viewModelScope.launch {
            runCatching {
                capabilityClient.addListener(capabilityChangedListener, "wear").await()
            }
        }
    }

    private fun removeCapabilityChangedListener() {
        coroutineScope.launch {
            withContext(NonCancellable) {
                runCatching {
                    addCapabilityChangedListenerJob?.cancel()
                    capabilityClient.removeListener(capabilityChangedListener, "wear").await()
                }
            }
        }
    }
}