package com.android.playground.phone.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.playground.device.DataLayerPath
import com.android.playground.device.DataLayerRequest
import com.google.android.gms.wearable.MessageClient
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DetailViewModel(private val messageClient: MessageClient) : ViewModel() {
    private val _uiState: MutableStateFlow<DetailUiState> = MutableStateFlow(DetailUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _effects: Channel<DetailEffect> = Channel(Channel.BUFFERED)
    val effects get() = _effects.receiveAsFlow()

    fun onIntent(intent: DetailIntent) {
        when (intent) {
            is DetailIntent.RequestConnect -> sendRequest(intent.nodeId)
            is DetailIntent.SetNodeOfWatch -> _uiState.update {
                it.copy(
                    nodeId = intent.node.id,
                    displayName = intent.node.displayName,
                    isNearby = intent.node.isNearby
                )
            }

            DetailIntent.OnNavigateBack -> _effects.trySend(DetailEffect.NavigateBack)
        }
    }

    fun sendRequest(nodeId: String) {
        viewModelScope.launch {
            runCatching {
                val response =
                    messageClient.sendRequest(
                        nodeId,
                        DataLayerPath.WEAR_CONNECT,
                        DataLayerRequest.CONNECT.toByteArray()
                    ).await()

                if (String(response).toBoolean()) {

                } else {

                }
            }
        }
    }
}