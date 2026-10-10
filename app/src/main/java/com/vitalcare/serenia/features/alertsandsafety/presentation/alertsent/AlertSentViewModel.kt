package com.vitalcare.serenia.features.alertsandsafety.presentation.alertsent

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AlertSentViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AlertSentUiState())
    val uiState: StateFlow<AlertSentUiState> = _uiState.asStateFlow()

    fun loadAlertSent() {
        // The family contacts will come from the user's safety network once it is available
        val contactNames = listOf("Lucía", "Martín")

        _uiState.update { currentState ->
            currentState.copy(
                contactNames = contactNames,
                message = buildMessage(contactNames)
            )
        }
    }

    private fun buildMessage(contactNames: List<String>): String = when (contactNames.size) {
        0 -> "La alerta fue enviada."
        1 -> "La alerta llegó a ${contactNames.first()}."
        else -> {
            val recipients = contactNames.dropLast(1).joinToString(", ") + " y a " + contactNames.last()
            "La alerta llegó a $recipients al mismo tiempo."
        }
    }

    init {
        loadAlertSent()
    }
}
