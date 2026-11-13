package com.vitalcare.serenia.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.core.format.formatLongDate
import com.vitalcare.serenia.core.format.greetingFor
import com.vitalcare.serenia.features.dailycheckin.application.ObserveCheckInPreferencesUseCase
import com.vitalcare.serenia.features.iam.application.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUser: GetCurrentUserUseCase,
    private val observeCheckInPreferences: ObserveCheckInPreferencesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    fun refreshGreeting() {
        val now = Calendar.getInstance()
        _state.update { it.copy(date = formatLongDate(now), greeting = greetingFor(now)) }
    }

    fun loadUser() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentUser().onSuccess { user ->
                _state.update { it.copy(firstName = user.fullName.trim().substringBefore(" ")) }
            }
        }
    }

    private fun observeSimplifiedMode() {
        viewModelScope.launch {
            observeCheckInPreferences().collect { preferences ->
                _state.update { it.copy(simplifiedMode = preferences?.simplifiedMode == true) }
            }
        }
    }

    init {
        refreshGreeting()
        observeSimplifiedMode()
    }
}
