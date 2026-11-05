package com.vitalcare.serenia.features.checkin.presentation.thanks

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.vitalcare.serenia.features.checkin.domain.Mood
import javax.inject.Inject

@HiltViewModel
class CheckInThanksViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CheckInThanksUiState())
    val uiState: StateFlow<CheckInThanksUiState> = _uiState.asStateFlow()

    fun loadThanks(mood: Mood) {
        // The user's name will come from the profile once it is available
        val userName = "Rosa"

        val familyNotice = when (mood) {
            Mood.GOOD -> "Tu familia sabrá que estás bien."
            Mood.NEUTRAL -> "Tu familia sabrá cómo te sientes."
            Mood.NOT_GOOD -> "Tu familia estará pendiente de ti."
        }

        _uiState.update { currentState ->
            currentState.copy(
                title = "Gracias, $userName. $familyNotice",
                message = "Tu familia ya puede ver cómo amaneciste."
            )
        }
    }
}
