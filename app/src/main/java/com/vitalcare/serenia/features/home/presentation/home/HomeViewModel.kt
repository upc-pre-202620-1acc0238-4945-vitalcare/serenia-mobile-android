package com.vitalcare.serenia.features.home.presentation.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.vitalcare.serenia.features.checkin.domain.Mood
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val dayNames = listOf("Dom", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb")
    private val monthNames = listOf(
        "ene", "feb", "mar", "abr", "may", "jun",
        "jul", "ago", "sep", "oct", "nov", "dic"
    )

    fun loadGreeting() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 5..11 -> "Buenos días"
            in 12..18 -> "Buenas tardes"
            else -> "Buenas noches"
        }

        val day = dayNames[calendar.get(Calendar.DAY_OF_WEEK) - 1]
        val month = monthNames[calendar.get(Calendar.MONTH)]
        val formattedDate = "$day ${calendar.get(Calendar.DAY_OF_MONTH)} $month"

        _uiState.update { currentState ->
            currentState.copy(
                // The user's name will come from the profile once it is available
                userName = "Rosa",
                greeting = greeting,
                formattedDate = formattedDate
            )
        }
    }

    fun selectMood(mood: Mood) {
        _uiState.update { currentState ->
            currentState.copy(selectedMood = mood, hasSkippedToday = false)
        }
    }

    fun skipToday() {
        _uiState.update { currentState ->
            currentState.copy(selectedMood = null, hasSkippedToday = true)
        }
    }

    init {
        loadGreeting()
    }
}
