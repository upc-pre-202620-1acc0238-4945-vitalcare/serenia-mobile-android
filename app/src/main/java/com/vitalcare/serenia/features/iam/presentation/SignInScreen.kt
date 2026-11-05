package com.vitalcare.serenia.features.iam.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.BackLink
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.SereniaTextField
import com.vitalcare.serenia.core.designsystems.components.TextLinkButton
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
    onSignedIn: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSignedIn) {
        if (state.isSignedIn) onSignedIn()
    }

    SignInContent(
        state = state,
        modifier = modifier,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onSubmit = viewModel::submit,
        onBack = onBack,
        onCreateAccount = onCreateAccount
    )
}

@Composable
fun SignInContent(
    state: SignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val submit = {
        focusManager.clearFocus()
        onSubmit()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(start = SereniaSpacing.authScreen, top = 12.dp, end = SereniaSpacing.authScreen, bottom = 24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            BackLink(onClick = onBack)
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineLarge,
                color = SereniaPalette.Teal700,
                modifier = Modifier.semantics { heading() }
            )
        }

        state.formError?.let { message ->
            AlertBanner(message = message)
        }

        SereniaTextField(
            label = "Correo o celular",
            value = state.email,
            onValueChange = onEmailChange,
            placeholder = "Ej. rosa@correo.com",
            error = state.emailError,
            isError = state.emailError != null || state.credentialsRejected,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
        )

        SereniaTextField(
            label = "Contraseña",
            value = state.password,
            onValueChange = onPasswordChange,
            placeholder = "Tu contraseña",
            error = state.passwordError,
            isError = state.passwordError != null || state.credentialsRejected,
            isPassword = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { submit() })
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 4.dp)
        ) {
            PrimaryButton(
                text = "Entrar",
                onClick = submit,
                isLoading = state.isLoading
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "¿Aún no tienes cuenta?",
                    fontFamily = Nunito,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SereniaPalette.Neutro700
                )
                TextLinkButton(
                    text = "Crear mi cuenta",
                    onClick = onCreateAccount,
                    color = SereniaPalette.Teal700,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun SignInContentPreview() {
    SereniaTheme {
        SignInContent(
            state = SignInUiState(
                email = "rosa@correo.com",
                credentialsRejected = true,
                formError = "El correo o la contraseña no son correctos. Revisa e intenta de nuevo."
            ),
            onEmailChange = {},
            onPasswordChange = {},
            onSubmit = {},
            onBack = {},
            onCreateAccount = {}
        )
    }
}
