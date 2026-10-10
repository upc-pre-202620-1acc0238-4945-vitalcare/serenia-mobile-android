package com.vitalcare.serenia.features.socialcompanionship.presentation.family

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.vitalcare.serenia.features.socialcompanionship.domain.FamilyPhoto
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FamilyUiState())
    val uiState: StateFlow<FamilyUiState> = _uiState.asStateFlow()

    fun loadPhotos() {
        // The photos will come from the linked family once it is available
        val photos = listOf(
            FamilyPhoto(id = 1, senderName = "Lucía", sentAt = "hoy"),
            FamilyPhoto(id = 2, senderName = "Martín", sentAt = "ayer")
        )

        _uiState.update { currentState ->
            currentState.copy(photos = photos)
        }
    }

    init {
        loadPhotos()
    }
}
