package com.vitalcare.serenia.features.alertsandsafety.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.features.alertsandsafety.application.RequestHelpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HelpViewModel @Inject constructor(
    private val requestHelp: RequestHelpUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HelpUiState())
    val state: StateFlow<HelpUiState> = _state.asStateFlow()

    fun sendHelpRequest() {
        if (_state.value.isSending) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isSending = true) }

            requestHelp().fold(
                onSuccess = { alert ->
                    val outcome = if (alert.familyNotified) HelpOutcome.NOTIFIED else HelpOutcome.NO_FAMILY
                    _state.update { it.copy(isSending = false, outcome = outcome) }
                },
                onFailure = {
                    _state.update { it.copy(isSending = false, outcome = HelpOutcome.FAILED) }
                }
            )
        }
    }

    fun onOutcomeShown() {
        _state.update { it.copy(outcome = null) }
    }
}
