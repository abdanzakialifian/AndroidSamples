package com.example.wear.presentation.connect

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {
    private val capabilityClient = Wearable.getCapabilityClient(context)

    private val messageClient = Wearable.getMessageClient(context)

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var addLocalCapabilityJob: Job? = null

    private var addMessageReceivedListenerJob: Job? = null

    private val messageReceivedListener = MessageClient.OnMessageReceivedListener {

    }

    fun onIntent(intent: ConnectIntent) {
        when (intent) {
            ConnectIntent.AddLocalCapability -> addLocalCapability()
            ConnectIntent.AddMessageReceivedListener -> addMessageReceivedListener()
            ConnectIntent.RemoveLocalCapability -> removeLocalCapability()
            ConnectIntent.RemoveMessageReceivedListener -> removeMessageReceivedListener()
        }
    }

    private fun addLocalCapability() {
        addLocalCapabilityJob?.start()
        addLocalCapabilityJob = viewModelScope.launch {
            runCatching {
                capabilityClient.addLocalCapability("wear").await()
            }
        }
    }

    private fun removeLocalCapability() {
        coroutineScope.launch {
            withContext(NonCancellable) {
                runCatching {
                    addLocalCapabilityJob?.cancel()
                    capabilityClient.removeLocalCapability("wear").await()
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
}