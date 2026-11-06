package com.vitalcare.serenia.features.account.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.BackLink
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.features.iam.domain.BirthDate
import com.vitalcare.serenia.features.iam.presentation.PersonalDataFields

@Composable
fun ProfileDataScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileDataViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }

    ProfileDataContent(
        state = state,
        modifier = modifier,
        onBack = onBack,
        onFullNameChange = viewModel::onFullNameChange,
        onBirthDateChange = viewModel::onBirthDateChange,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        onSave = viewModel::save
    )
}

@Composable
fun ProfileDataContent(
    state: ProfileDataUiState,
    onBack: () -> Unit,
    onFullNameChange: (String) -> Unit,
    onBirthDateChange: (BirthDate) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

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
                text = "Mis datos",
                style = MaterialTheme.typography.headlineLarge,
                color = SereniaPalette.Teal700,
                modifier = Modifier.semantics { heading() }
            )
        }

        if (state.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp)
            ) {
                CircularProgressIndicator(color = SereniaPalette.Teal700)
            }
            return@Column
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(88.dp)
                .background(SereniaPalette.Durazno300, CircleShape)
        ) {
            Text(
                text = state.initial,
                fontFamily = Nunito,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Durazno900
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

        PrimaryButton(
            text = "Guardar cambios",
            onClick = {
                focusManager.clearFocus()
                onSave()
            },
            isLoading = state.isSaving
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun ProfileDataContentPreview() {
    SereniaTheme {
        ProfileDataContent(
            state = ProfileDataUiState(
                isLoading = false,
                fullName = "Rosa Mendoza",
                birthDate = BirthDate(1948, 5, 12),
                phoneNumber = "987654321"
            ),
            onBack = {},
            onFullNameChange = {},
            onBirthDateChange = {},
            onPhoneNumberChange = {},
            onSave = {}
        )
    }
}
