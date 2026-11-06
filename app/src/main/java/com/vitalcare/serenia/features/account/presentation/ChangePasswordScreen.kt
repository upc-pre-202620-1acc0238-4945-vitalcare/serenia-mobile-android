package com.vitalcare.serenia.features.account.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.BackLink
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.SereniaTextField
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChangePasswordViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }

    ChangePasswordContent(
        state = state,
        modifier = modifier,
        onBack = onBack,
        onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
        onNewPasswordChange = viewModel::onNewPasswordChange,
        onSave = viewModel::save
    )
}

@Composable
fun ChangePasswordContent(
    state: ChangePasswordUiState,
    onBack: () -> Unit,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val save = {
        focusManager.clearFocus()
        onSave()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(start = SereniaSpacing.screen, top = 12.dp, end = SereniaSpacing.screen, bottom = 24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BackLink(onClick = onBack, text = "Mi cuenta")
            Text(
                text = "Cambiar mi contraseña",
                style = MaterialTheme.typography.headlineLarge,
                color = SereniaPalette.Teal700,
                modifier = Modifier.semantics { heading() }
            )
        }

        state.formError?.let { message ->
            AlertBanner(message = message)
        }

        SereniaTextField(
            label = "Contraseña actual",
            value = state.currentPassword,
            onValueChange = onCurrentPasswordChange,
            error = state.currentPasswordError,
            isPassword = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        SereniaTextField(
            label = "Contraseña nueva",
            value = state.newPassword,
            onValueChange = onNewPasswordChange,
            error = state.newPasswordError,
            helper = "Mínimo 8 letras o números.",
            isPassword = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { save() })
        )

        Text(
            text = "Por seguridad, cerraremos tu sesión en otros celulares.",
            style = MaterialTheme.typography.bodySmall,
            color = SereniaPalette.Neutro700
        )

        PrimaryButton(
            text = "Guardar contraseña",
            onClick = save,
            isLoading = state.isSaving
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun ChangePasswordContentPreview() {
    SereniaTheme {
        ChangePasswordContent(
            state = ChangePasswordUiState(
                currentPassword = "secreto12",
                currentPasswordError = "Esa no es tu contraseña actual. Intenta de nuevo."
            ),
            onBack = {},
            onCurrentPasswordChange = {},
            onNewPasswordChange = {},
            onSave = {}
        )
    }
}
