package com.vitalcare.serenia.features.carecircle.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.carecircle.application.GenerateInvitationCodeUseCase
import com.vitalcare.serenia.features.carecircle.application.GetInvitationCodeUseCase
import com.vitalcare.serenia.features.carecircle.application.RevokeFamilyLinkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CircleViewModel @Inject constructor(
    private val getInvitationCode: GetInvitationCodeUseCase,
    private val generateInvitationCode: GenerateInvitationCodeUseCase,
    private val revokeFamilyLink: RevokeFamilyLinkUseCase,
    private val getFamilyMembers: FamilyMembersProvider
) : ViewModel() {

    private val _state = MutableStateFlow(CircleUiState())
    val state: StateFlow<CircleUiState> = _state.asStateFlow()

    fun refresh() {
        loadInvitationCode()
        loadMembers()
    }

    fun generateNewCode() {
        if (_state.value.isGeneratingCode) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isGeneratingCode = true, codeError = null) }

            generateInvitationCode().fold(
                onSuccess = { invitationCode ->
                    _state.update { it.copy(invitationCode = invitationCode, isGeneratingCode = false) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isGeneratingCode = false, codeError = exception.toUserMessage()) }
                }
            )
        }
    }

    fun askToRemove(familyLinkId: String) {
        if (_state.value.isRemoving) return
        _state.update { it.copy(memberToRemove = familyLinkId, removeError = null) }
    }

    fun cancelRemove() {
        if (_state.value.isRemoving) return
        _state.update { it.copy(memberToRemove = null, removeError = null) }
    }

    fun confirmRemove() {
        val familyLinkId = _state.value.memberToRemove ?: return
        if (_state.value.isRemoving) return

        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isRemoving = true, removeError = null) }

            revokeFamilyLink(familyLinkId).fold(
                onSuccess = {
                    _state.update { state ->
                        state.copy(
                            members = state.members?.filterNot { it.familyLinkId == familyLinkId },
                            memberToRemove = null,
                            isRemoving = false
                        )
                    }
                },
                onFailure = { exception ->
                    _state.update { it.copy(isRemoving = false, removeError = exception.toUserMessage()) }
                }
            )
        }
    }

    private fun loadInvitationCode() {
        viewModelScope.launch(Dispatchers.IO) {
            getInvitationCode().fold(
                onSuccess = { invitationCode ->
                    _state.update { it.copy(invitationCode = invitationCode, codeError = null) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(codeError = exception.toUserMessage()) }
                }
            )
        }
    }

    private fun loadMembers() {
        viewModelScope.launch(Dispatchers.IO) {
            getFamilyMembers().fold(
                onSuccess = { members ->
                    _state.update { it.copy(members = members, membersError = null) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(membersError = exception.toUserMessage()) }
                }
            )
        }
    }
}
