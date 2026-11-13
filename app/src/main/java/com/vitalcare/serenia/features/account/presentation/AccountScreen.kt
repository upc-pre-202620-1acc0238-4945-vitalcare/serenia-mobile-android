package com.vitalcare.serenia.features.account.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.ChosenValueCard
import com.vitalcare.serenia.core.designsystems.components.FieldError
import com.vitalcare.serenia.core.designsystems.components.QuietButton
import com.vitalcare.serenia.core.designsystems.components.SereniaSnackbar
import com.vitalcare.serenia.core.designsystems.components.SwitchRow
import com.vitalcare.serenia.core.designsystems.components.TimeInputDialog
import com.vitalcare.serenia.core.designsystems.contrast
import com.vitalcare.serenia.core.designsystems.lock
import com.vitalcare.serenia.core.designsystems.logout
import com.vitalcare.serenia.core.designsystems.simplifiedMode
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.format.formatTime
import com.vitalcare.serenia.features.dailycheckin.domain.ReminderTime
import kotlinx.coroutines.delay

@Composable
fun AccountScreen(
    message: String?,
    onMessageShown: () -> Unit,
    onEditProfile: () -> Unit,
    onChangePassword: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.loadUser()
        onPauseOrDispose { }
    }

    LaunchedEffect(message) {
        if (message != null) {
            delay(MESSAGE_DURATION_MILLIS)
            onMessageShown()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        AccountContent(
            state = state,
            onEditProfile = onEditProfile,
            onChangeTime = viewModel::openPicker,
            onSimplifiedModeChange = viewModel::changeSimplifiedMode,
            onHighContrastChange = viewModel::changeHighContrast,
            onChangePassword = onChangePassword,
            onSignOut = viewModel::askToSignOut,
            onConfirmSignOut = viewModel::confirmSignOut,
            onCancelSignOut = viewModel::cancelSignOut
        )

        message?.let { text ->
            SereniaSnackbar(
                message = text,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = SereniaSpacing.md, vertical = SereniaSpacing.md)
            )
        }
    }

    if (state.isPickerVisible) {
        val time = state.displayedTime ?: ReminderTime.DEFAULT
        TimeInputDialog(
            title = "¿A qué hora?",
            initialHour = time.hour,
            initialMinute = time.minute,
            onConfirm = viewModel::onTimeChosen,
            onDismiss = viewModel::closePicker
        )
    }
}

@Composable
fun AccountContent(
    state: AccountUiState,
    onEditProfile: () -> Unit,
    onChangeTime: () -> Unit,
    onSimplifiedModeChange: (Boolean) -> Unit,
    onHighContrastChange: (Boolean) -> Unit,
    onChangePassword: () -> Unit,
    onSignOut: () -> Unit,
    onConfirmSignOut: () -> Unit,
    onCancelSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
            .verticalScroll(rememberScrollState())
            .padding(start = SereniaSpacing.screen, top = 24.dp, end = SereniaSpacing.screen, bottom = 24.dp)
    ) {
        Text(
            text = "Mi cuenta",
            style = MaterialTheme.typography.headlineLarge,
            letterSpacing = (-0.005).em,
            color = SereniaPalette.Teal700,
            modifier = Modifier.semantics { heading() }
        )

        ProfileCard(fullName = state.fullName, photoUrl = state.photoUrl, onClick = onEditProfile)

        SectionTitle(text = "Mi pregunta del día")
        state.displayedTime?.let { time ->
            ChosenValueCard(
                label = "Hora de tu pregunta",
                value = formatTime(time.hour, time.minute),
                onChange = onChangeTime,
                isError = state.timeError != null,
                errorMessage = state.timeError
            )
        }
        state.timeError?.let { message ->
            FieldError(message = message)
        }

        SectionTitle(text = "Cómo se ve la app")
        SwitchRow(
            icon = simplifiedMode,
            title = "Modo simplificado",
            description = "Solo cómo estás y pedir ayuda",
            checked = state.simplifiedMode,
            onCheckedChange = onSimplifiedModeChange,
            enabled = !state.isSavingSimplifiedMode,
            iconBackground = SereniaPalette.Durazno200,
            iconTint = SereniaPalette.Durazno900
        )
        SwitchRow(
            icon = contrast,
            title = "Alto contraste",
            description = "Letras y bordes más marcados",
            checked = state.highContrast,
            onCheckedChange = onHighContrastChange
        )
        state.appearanceError?.let { message ->
            AlertBanner(message = message)
        }

        SectionTitle(text = "Seguridad")
        AccountOptionRow(icon = lock, text = "Cambiar mi contraseña", onClick = onChangePassword)

        if (state.isConfirmingSignOut) {
            SignOutConfirmationCard(
                isSigningOut = state.isSigningOut,
                errorMessage = state.signOutError,
                onConfirm = onConfirmSignOut,
                onCancel = onCancelSignOut
            )
        } else {
            QuietButton(
                text = "Cerrar sesión",
                icon = logout,
                onClick = onSignOut,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = SereniaPalette.Neutro600,
        modifier = Modifier
            .padding(top = 6.dp)
            .semantics { heading() }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun AccountContentPreview() {
    SereniaTheme {
        AccountContent(
            state = AccountUiState(fullName = "Rosa Mendoza", reminderTime = ReminderTime.DEFAULT),
            onEditProfile = {},
            onChangeTime = {},
            onSimplifiedModeChange = {},
            onHighContrastChange = {},
            onChangePassword = {},
            onSignOut = {},
            onConfirmSignOut = {},
            onCancelSignOut = {}
        )
    }
}

private const val MESSAGE_DURATION_MILLIS = 4_000L
