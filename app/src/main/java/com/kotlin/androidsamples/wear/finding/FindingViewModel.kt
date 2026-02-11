package com.kotlin.androidsamples.wear.finding

import androidx.lifecycle.ViewModel
import com.google.android.gms.wearable.Node
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FindingViewModel @Inject constructor() : ViewModel() {
    private val _uiState: MutableStateFlow<FindingUiState> = MutableStateFlow(FindingUiState())
    val uiState: StateFlow<FindingUiState> = _uiState

    fun setNodes(nodes: Set<Node>) {
        _uiState.update { it.copy(nodes = nodes) }
    }
}