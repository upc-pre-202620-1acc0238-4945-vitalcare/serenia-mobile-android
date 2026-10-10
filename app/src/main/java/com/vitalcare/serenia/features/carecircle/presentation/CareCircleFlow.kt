package com.vitalcare.serenia.features.carecircle.presentation

import androidx.compose.runtime.*

@Composable
fun CareCircleFlow() {
    var currentScreen by remember { mutableStateOf("CREATE_CIRCLE") }

    when (currentScreen) {
        "CREATE_CIRCLE" -> CreateCircleScreen(
            onNavigateToCode = { currentScreen = "INVITATION_CODE" }
        )
        "INVITATION_CODE" -> InvitationCodeScreen(
            onCopyCodeClick = { /* Acción simulada */ },
            onNavigateNext = { currentScreen = "MY_CIRCLE" }
        )
        "MY_CIRCLE" -> MyCircleScreen(
            onBackClick = { currentScreen = "INVITATION_CODE" }
        )
    }
}