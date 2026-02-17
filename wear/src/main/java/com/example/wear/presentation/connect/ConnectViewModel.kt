package com.example.wear.presentation.connect

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.wearable.Wearable
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ConnectViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {
    val capabilityClient = Wearable.getCapabilityClient(context)

    suspend fun addLocalCapability() {
        try {
            capabilityClient.addLocalCapability("wear").await()
        } catch (cancellation: CancellationException) {
            throw cancellation
        } catch (e: Exception) {
            Log.d("CEK", "ERROR : $e")
        }
    }

    suspend fun removeLocalCapability() {
        withContext(NonCancellable) {
            try {
                capabilityClient.removeLocalCapability("wear").await()
            } catch (e: Exception) {
                Log.d("CEK", "ERROR : $e")
            }
        }
    }
}