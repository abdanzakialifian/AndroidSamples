package com.android.playground.watch.presentation.discoverable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playground.device.DataLayerPath
import com.android.playground.device.DataLayerRequest
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.MessageClient
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DiscoverableViewModel(
    private val messageClient: MessageClient,
    private val capabilityClient: CapabilityClient
) : ViewModel() {
    private val _uiState: MutableStateFlow<DiscoverableUiState> =
        MutableStateFlow(DiscoverableUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _effects: Channel<DiscoverableEffect> = Channel(Channel.BUFFERED)
    val effects get() = _effects.receiveAsFlow()

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var addLocalCapabilityJob: Job? = null

    private var addMessageReceivedListenerJob: Job? = null

    private var addRpcServiceListenerJob: Job? = null

    private val messageReceivedListener = MessageClient.OnMessageReceivedListener { }

    private val rpcServiceListener = MessageClient.RpcService { _, path, byteArray ->
        when (path) {
            DataLayerPath.WEAR_CONNECT -> setConnected(byteArray)
            else -> Tasks.forResult(byteArrayOf())
        }
    }

    init {
        onIntent(DiscoverableIntent.RemoveLocalCapability)
    }

    fun onIntent(intent: DiscoverableIntent) {
        when (intent) {
            DiscoverableIntent.AddLocalCapability -> addLocalCapability()
            DiscoverableIntent.AddMessageReceivedListener -> addMessageReceivedListener()
            DiscoverableIntent.RemoveLocalCapability -> removeLocalCapability()
            DiscoverableIntent.RemoveMessageReceivedListener -> removeMessageReceivedListener()
            DiscoverableIntent.AddRpcServiceListener -> addRpcServiceListener()
            DiscoverableIntent.RemoveRpcServiceListener -> removeRpcServiceListener()
        }
    }

    private fun addLocalCapability() {
        addLocalCapabilityJob?.start()
        addLocalCapabilityJob = viewModelScope.launch {
            runCatching {
                capabilityClient.addLocalCapability(DataLayerPath.WEAR_CAPABILITY).await()
            }
        }
    }

    private fun removeLocalCapability() {
        coroutineScope.launch {
            withContext(NonCancellable) {
                runCatching {
                    addLocalCapabilityJob?.cancel()
                    capabilityClient.removeLocalCapability(DataLayerPath.WEAR_CAPABILITY).await()
                }
            }
        }
    }

    private fun addMessageReceivedListener() {
        addMessageReceivedListenerJob?.start()
        addMessageReceivedListenerJob = viewModelScope.launch {
            runCatching {
                messageClient.addListener(messageReceivedListener).await()
            }
        }
    }

    private fun removeMessageReceivedListener() {
        coroutineScope.launch {
            withContext(NonCancellable) {
                runCatching {
                    addMessageReceivedListenerJob?.cancel()
                    messageClient.removeListener(messageReceivedListener).await()
                }
            }
        }
    }

    private fun addRpcServiceListener() {
        addRpcServiceListenerJob?.start()
        addRpcServiceListenerJob = viewModelScope.launch {
            runCatching {
                messageClient.addRpcService(rpcServiceListener, DataLayerPath.ALL).await()
            }
        }
    }

    private fun removeRpcServiceListener() {
        coroutineScope.launch {
            withContext(NonCancellable) {
                runCatching {
                    addRpcServiceListenerJob?.cancel()
                    messageClient.removeRpcService(rpcServiceListener).await()
                }
            }
        }
    }

    private fun setConnected(byteArray: ByteArray): Task<ByteArray> {
        if (!byteArray.contentEquals(DataLayerRequest.CONNECT.toByteArray())) {
            return Tasks.forResult(false.toString().toByteArray())
        }

        val taskCompletionSource = TaskCompletionSource<ByteArray>()
        viewModelScope.launch {
            delay(3000L)
            _effects.trySend(DiscoverableEffect.GoToNextScreen)
            taskCompletionSource.setResult(true.toString().toByteArray())
        }
        return taskCompletionSource.task
    }
}