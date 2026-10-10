package com.vitalcare.serenia.features.iam.presentation.signin

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
import androidx.compose.ui.Alignment
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
import com.vitalcare.serenia.core.designsystem.components.SereniaTextLink
import com.vitalcare.serenia.core.designsystem.icon.lock
import com.vitalcare.serenia.core.designsystem.icon.mail
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.features.iam.presentation.components.BackLink
import com.vitalcare.serenia.features.iam.presentation.components.FormErrorBanner
import com.vitalcare.serenia.features.iam.presentation.components.SignInHeaderCard

private const val ERROR_MESSAGE =
    "Revisa los campos marcados: escribe tu correo o celular y tu contraseña."

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
    onSignedIn: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    SignInContent(
        uiState = uiState,
        modifier = modifier,
        onIdentifierChange = viewModel::onIdentifierChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = {
            if (viewModel.submit()) onSignedIn()
        },
        onBack = onBack,
        onCreateAccount = onCreateAccount
    )
}

@Composable
fun SignInContent(
    uiState: SignInUiState,
    onIdentifierChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
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
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            BackLink(onClick = onBack)

            Spacer(modifier = Modifier.height(8.dp))

            SignInHeaderCard(
                title = "Qué bueno verte de nuevo",
                subtitle = "Entra y mira cómo va tu día."
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                placeholder = "Tu contraseña",
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
                text = "Iniciar sesión",
                onClick = onSubmit,
                fontFamily = FontFamily.Default,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            SereniaTextLink(
                text = "¿Nuevo en Serenia? Crear cuenta",
                onClick = onCreateAccount,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9, heightDp = 780)
@Composable
fun SignInScreenPreview() {
    SereniaTheme {
        SignInContent(
            uiState = SignInUiState(),
            onIdentifierChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onBack = {},
            onCreateAccount = {}
        )
    }
}
