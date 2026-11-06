package com.vitalcare.serenia.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.features.dailycheckin.application.ClearCheckInPreferencesUseCase
import com.vitalcare.serenia.features.dailycheckin.application.ObserveCheckInPreferencesUseCase
import com.vitalcare.serenia.features.iam.application.IsSignedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    isSignedIn: IsSignedInUseCase,
    observeCheckInPreferences: ObserveCheckInPreferencesUseCase,
    clearCheckInPreferences: ClearCheckInPreferencesUseCase
) : ViewModel() {

    val isSignedIn: StateFlow<Boolean?> = isSignedIn()
        .onEach { signedIn -> if (!signedIn) clearCheckInPreferences() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val simplifiedMode: StateFlow<Boolean> = observeCheckInPreferences()
        .map { preferences -> preferences?.simplifiedMode == true }
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
}
