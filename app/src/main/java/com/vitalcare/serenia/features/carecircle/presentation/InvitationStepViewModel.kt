package com.vitalcare.serenia.features.carecircle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.carecircle.application.GetInvitationCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationStepViewModel @Inject constructor(
    private val getInvitationCode: GetInvitationCodeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(InvitationStepUiState())
    val state: StateFlow<InvitationStepUiState> = _state.asStateFlow()

    fun loadInvitationCode() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(error = null) }

            getInvitationCode().fold(
                onSuccess = { invitationCode ->
                    _state.update { it.copy(invitationCode = invitationCode) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(error = exception.toUserMessage()) }
                }
            )
        }
    }

    init {
        loadInvitationCode()
    }
}
