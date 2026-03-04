package com.android.playground.phone.finding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
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
import java.lang.ref.WeakReference

class FindingViewModel : ViewModel() {
    private val _uiState: MutableStateFlow<FindingUiState> = MutableStateFlow(FindingUiState())
    val uiState: StateFlow<FindingUiState> = _uiState

    private var context: WeakReference<Context>? = null

    val capabilityClient = context?.get()?.let { Wearable.getCapabilityClient(it) }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var addCapabilityChangedListenerJob: Job? = null

    val capabilityChangedListener = CapabilityClient.OnCapabilityChangedListener { capabilityInfo ->
        _uiState.update { it.copy(nodes = capabilityInfo.nodes) }
    }

    init {
        onIntent(FindingIntent.StartCountdown)
    }

    fun attachContext(context: Context) {
        this.context = WeakReference(context)
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
                    capabilityClient?.getCapability("wear", CapabilityClient.FILTER_REACHABLE)
                        ?.await()
                _uiState.update { it.copy(nodes = capabilityInfo?.nodes.orEmpty()) }
            }
        }
    }

    private fun addCapabilityChangedListener() {
        addCapabilityChangedListenerJob?.start()
        addCapabilityChangedListenerJob = viewModelScope.launch {
            runCatching {
                capabilityClient?.addListener(capabilityChangedListener, "wear")?.await()
            }
        }
    }

    private fun removeCapabilityChangedListener() {
        coroutineScope.launch {
            withContext(NonCancellable) {
                runCatching {
                    addCapabilityChangedListenerJob?.cancel()
                    capabilityClient?.removeListener(capabilityChangedListener, "wear")?.await()
                }
            }
        }
    }
}