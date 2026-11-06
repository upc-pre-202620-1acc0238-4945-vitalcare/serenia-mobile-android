package com.vitalcare.serenia.features.iam.presentation

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.BackLink
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.SereniaTextField
import com.vitalcare.serenia.core.designsystems.components.StepIndicator
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.features.iam.domain.BirthDate

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSignedUp: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSignedUp) {
        if (state.isSignedUp) onSignedUp()
    }

    SignUpContent(
        state = state,
        modifier = modifier,
        onFullNameChange = viewModel::onFullNameChange,
        onBirthDateChange = viewModel::onBirthDateChange,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = viewModel::submit,
        onBack = onBack
    )
}

@Composable
fun SignUpContent(
    state: SignUpUiState,
    onFullNameChange: (String) -> Unit,
    onBirthDateChange: (BirthDate) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val submit = {
        focusManager.clearFocus()
        onSubmit()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(start = SereniaSpacing.authScreen, top = 12.dp, end = SereniaSpacing.authScreen, bottom = 24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BackLink(onClick = onBack)
            StepIndicator(current = 1)
            Text(
                text = "Crea tu cuenta",
                style = MaterialTheme.typography.headlineLarge,
                color = SereniaPalette.Teal700,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = "Solo necesitamos tus datos básicos.",
                style = MaterialTheme.typography.bodyMedium,
                color = SereniaPalette.Neutro700
            )
        }

        state.formError?.let { message ->
            AlertBanner(message = message)
        }

        PersonalDataFields(
            fullName = state.fullName,
            fullNameError = state.fullNameError,
            onFullNameChange = onFullNameChange,
            birthDate = state.birthDate,
            onBirthDateChange = onBirthDateChange,
            phoneNumber = state.phoneNumber,
            phoneNumberError = state.phoneNumberError,
            onPhoneNumberChange = onPhoneNumberChange
        )

        SereniaTextField(
            label = "Correo",
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = "Ej. rosa@correo.com",
            error = state.emailError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )

        SereniaTextField(
            label = "Contraseña",
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = "Mínimo 8 letras o números",
            error = state.passwordError,
            helper = "Mínimo 8 letras o números.",
            isPassword = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { submit() })
        )

        PrimaryButton(
            text = "Crear mi cuenta",
            onClick = submit,
            isLoading = state.isLoading
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun SignUpContentPreview() {
    SereniaTheme {
        SignUpContent(
            state = SignUpUiState(
                email = "rosa@correo",
                password = "rosa1",
                fullNameError = "Escribe tu nombre.",
                emailError = "Revisa el correo: parece incompleto.",
                passwordError = "Debe tener al menos 8 letras o números."
            ),
            onFullNameChange = {},
            onBirthDateChange = {},
            onPhoneNumberChange = {},
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onBack = {}
        )
    }
}
