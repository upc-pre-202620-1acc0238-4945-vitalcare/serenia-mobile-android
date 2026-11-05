package com.vitalcare.serenia.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vitalcare.serenia.features.iam.application.IsSignedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(isSignedIn: IsSignedInUseCase) : ViewModel() {

    val isSignedIn: StateFlow<Boolean?> = isSignedIn()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}
