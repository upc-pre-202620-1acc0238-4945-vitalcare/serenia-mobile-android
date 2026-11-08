package com.vitalcare.serenia.core.appearance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppearanceViewModel @Inject constructor(storage: AppearanceStorage) : ViewModel() {

    val highContrast: StateFlow<Boolean> = storage.highContrast
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)
}
