package com.android.playground.watch.presentation.connect

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.ref.WeakReference

class ConnectViewModel : ViewModel() {
    private var context: WeakReference<Context>? = null

    private val capabilityClient = context?.get()?.let { Wearable.getCapabilityClient(it) }

    private val messageClient = context?.get()?.let { Wearable.getMessageClient(it) }

    private val coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var addLocalCapabilityJob: Job? = null

    private var addMessageReceivedListenerJob: Job? = null

    private val messageReceivedListener = MessageClient.OnMessageReceivedListener {

    }

    fun attachContext(context: Context) {
        this.context = WeakReference(context)
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
                capabilityClient?.addLocalCapability("wear")?.await()
            }
        }
    }

    private fun removeLocalCapability() {
        coroutineScope.launch {
            withContext(NonCancellable) {
                runCatching {
                    addLocalCapabilityJob?.cancel()
                    capabilityClient?.removeLocalCapability("wear")?.await()
                }
            }
        }
    }

    private fun addMessageReceivedListener() {
        addMessageReceivedListenerJob?.start()
        addMessageReceivedListenerJob = viewModelScope.launch {
            runCatching {
                messageClient?.addListener(messageReceivedListener)?.await()
            }
        }
    }

    private fun removeMessageReceivedListener() {
        coroutineScope.launch {
            withContext(NonCancellable) {
                runCatching {
                    addMessageReceivedListenerJob?.cancel()
                    messageClient?.removeListener(messageReceivedListener)?.await()
                }
            }
        }
    }
}