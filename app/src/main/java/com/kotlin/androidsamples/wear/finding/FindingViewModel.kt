package com.kotlin.androidsamples.wear.finding

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
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

    val capabilityChangedListener = CapabilityClient.OnCapabilityChangedListener { capabilityInfo ->
        _uiState.update { it.copy(nodes = capabilityInfo.nodes) }
    }

    fun startCountdown() {
        viewModelScope.launch {
            _uiState.update { it.copy(time = 15) }
            while (uiState.value.time > 0) {
                delay(1000L)
                _uiState.update { it.copy(time = it.time - 1) }
            }
        }
    }

    suspend fun getCapability() {
        try {
            val capabilityInfo =
                capabilityClient.getCapability("wear", CapabilityClient.FILTER_REACHABLE).await()
            _uiState.update { it.copy(nodes = capabilityInfo.nodes) }
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (e: Exception) {
            Log.d("CEK", "ERROR : $e")
        }
    }

    suspend fun addListener() {
        try {
            capabilityClient.addListener(capabilityChangedListener, "wear").await()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (e: Exception) {
            Log.d("CEK", "ERROR : $e")
        }
    }

    suspend fun removeListener() {
        withContext(NonCancellable) {
            try {
                capabilityClient.removeListener(capabilityChangedListener, "wear").await()
            } catch (e: Exception) {
                Log.d("CEK", "ERROR : $e")
            }
        }
    }
}