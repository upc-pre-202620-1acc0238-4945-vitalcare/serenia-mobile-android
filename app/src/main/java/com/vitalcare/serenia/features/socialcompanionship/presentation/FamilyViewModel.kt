package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.network.toUserMessage
import com.vitalcare.serenia.features.carecircle.presentation.FamilyMembersProvider
import com.vitalcare.serenia.features.socialcompanionship.application.GetPhotoMessagesUseCase
import com.vitalcare.serenia.features.socialcompanionship.application.ViewPhotoMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val getPhotoMessages: GetPhotoMessagesUseCase,
    private val viewPhotoMessage: ViewPhotoMessageUseCase,
    private val getFamilyMembers: FamilyMembersProvider
) : ViewModel() {

    private val _state = MutableStateFlow(FamilyUiState())
    val state: StateFlow<FamilyUiState> = _state.asStateFlow()

    fun refresh() {
        loadMembers()
        loadPhotos()
    }

    fun markAsViewed(photoMessageId: String) {
        val photo = _state.value.photoMessages?.firstOrNull { it.id == photoMessageId } ?: return
        if (photo.isViewed) return

        _state.update { state ->
            state.copy(photoMessages = state.photoMessages?.map {
                if (it.id == photoMessageId) it.copy(isViewed = true) else it
            })
        }
        viewModelScope.launch(Dispatchers.IO) {
            viewPhotoMessage(photoMessageId)
        }
    }

    fun showMessage(message: String) {
        _state.update { it.copy(message = message) }
    }

    fun onMessageShown() {
        _state.update { it.copy(message = null) }
    }

    private fun loadMembers() {
        viewModelScope.launch(Dispatchers.IO) {
            getFamilyMembers().onSuccess { members ->
                _state.update { it.copy(members = members) }
            }
        }
    }

    private fun loadPhotos() {
        viewModelScope.launch(Dispatchers.IO) {
            getPhotoMessages().fold(
                onSuccess = { photos ->
                    _state.update { it.copy(photoMessages = photos, loadError = null) }
                },
                onFailure = { exception ->
                    _state.update { it.copy(loadError = exception.toUserMessage()) }
                }
            )
        }
    }
}
