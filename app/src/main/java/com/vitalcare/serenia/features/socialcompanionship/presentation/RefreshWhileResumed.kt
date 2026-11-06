package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay

@Composable
fun RefreshWhileResumed(onRefresh: () -> Unit, intervalMillis: Long = REFRESH_INTERVAL_MILLIS) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnRefresh by rememberUpdatedState(onRefresh)

    LaunchedEffect(lifecycleOwner, intervalMillis) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            while (true) {
                currentOnRefresh()
                delay(intervalMillis)
            }
        }
    }
}

private const val REFRESH_INTERVAL_MILLIS = 60_000L
