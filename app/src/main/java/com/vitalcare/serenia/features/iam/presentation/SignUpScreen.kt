package com.vitalcare.serenia.features.iam.presentation.signup

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystem.components.SereniaBackground
import com.vitalcare.serenia.core.designsystem.components.SereniaPrimaryButton
import com.vitalcare.serenia.core.designsystem.components.SereniaTextField
import com.vitalcare.serenia.core.designsystem.icon.lock
import com.vitalcare.serenia.core.designsystem.icon.mail
import com.vitalcare.serenia.core.designsystem.icon.personOutline
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.features.iam.presentation.components.BackLink
import com.vitalcare.serenia.features.iam.presentation.components.FormErrorBanner
import com.vitalcare.serenia.features.iam.presentation.components.SignUpHeaderCard

private const val ERROR_MESSAGE =
    "Revisa los campos marcados: escribe tu nombre, un correo válido y una contraseña de al menos 8 caracteres."

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onSignedUp: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    SignUpContent(
        uiState = uiState,
        modifier = modifier,
        onNameChange = viewModel::onNameChange,
        onIdentifierChange = viewModel::onIdentifierChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = {
            if (viewModel.submit()) onSignedUp()
        },
        onBack = onBack
    )
}

@Composable
fun SignUpContent(
    uiState: SignUpUiState,
    onNameChange: (String) -> Unit,
    onIdentifierChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    SereniaBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            SignUpHeaderCard(
                title = "Crea tu cuenta",
                subtitle = "Solo necesitamos tus datos básicos para vincularte."
            )

            Spacer(modifier = Modifier.height(16.dp))

            SereniaTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                label = "Tu nombre",
                placeholder = "Ej. Lucía Mendoza",
                leadingIcon = personOutline,
                isError = uiState.nameError
            )

            Spacer(modifier = Modifier.height(12.dp))

            SereniaTextField(
                value = uiState.identifier,
                onValueChange = onIdentifierChange,
                label = "Correo o celular",
                placeholder = "tucorreo@ejemplo.com",
                leadingIcon = mail,
                isError = uiState.identifierError,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(12.dp))

            SereniaTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                label = "Contraseña",
                placeholder = "Mínimo 8 caracteres",
                leadingIcon = lock,
                isError = uiState.passwordError,
                isPassword = true,
                imeAction = ImeAction.Done,
                onImeAction = onSubmit
            )

            if (uiState.hasErrors) {
                Spacer(modifier = Modifier.height(12.dp))
                FormErrorBanner(message = ERROR_MESSAGE)
            }

            Spacer(modifier = Modifier.height(16.dp))

            SereniaPrimaryButton(
                text = "Crear cuenta",
                onClick = onSubmit,
                fontFamily = FontFamily.Default,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            BackLink(onClick = onBack)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9, heightDp = 780)
@Composable
fun SignUpScreenPreview() {
    SereniaTheme {
        SignUpContent(
            uiState = SignUpUiState(),
            onNameChange = {},
            onIdentifierChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9, heightDp = 780)
@Composable
fun SignUpScreenErrorPreview() {
    SereniaTheme {
        SignUpContent(
            uiState = SignUpUiState(nameError = true, identifierError = true, passwordError = true),
            onNameChange = {},
            onIdentifierChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onBack = {}
        )
    }
}
