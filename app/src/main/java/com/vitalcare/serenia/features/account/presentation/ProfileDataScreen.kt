package com.vitalcare.serenia.features.account.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.BackLink
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.ProfileAvatar
import com.vitalcare.serenia.core.designsystems.components.SecondaryButton
import com.vitalcare.serenia.core.designsystems.components.SereniaSnackbar
import com.vitalcare.serenia.core.designsystems.photoCamera
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.features.iam.domain.BirthDate
import com.vitalcare.serenia.features.iam.presentation.PersonalDataFields
import kotlinx.coroutines.delay

@Composable
fun ProfileDataScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileDataViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val photoPicker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { viewModel.uploadPhoto(it.toString()) }
    }

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) onSaved()
    }

    LaunchedEffect(state.message) {
        if (state.message != null) {
            delay(MESSAGE_DURATION_MILLIS)
            viewModel.onMessageShown()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        ProfileDataContent(
            state = state,
            onBack = onBack,
            onChangePhoto = {
                photoPicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onFullNameChange = viewModel::onFullNameChange,
            onBirthDateChange = viewModel::onBirthDateChange,
            onPhoneNumberChange = viewModel::onPhoneNumberChange,
            onSave = viewModel::save
        )

        state.message?.let { text ->
            SereniaSnackbar(
                message = text,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(SereniaSpacing.md)
            )
        }
    }
}

@Composable
fun ProfileDataContent(
    state: ProfileDataUiState,
    onBack: () -> Unit,
    onChangePhoto: () -> Unit,
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

        Row(
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileAvatar(
                fullName = state.fullName,
                photoUrl = state.photoUrl,
                size = 88.dp,
                isLoading = state.isUploadingPhoto
            )
            SecondaryButton(
                text = "Cambiar foto",
                icon = photoCamera,
                onClick = onChangePhoto,
                enabled = !state.isUploadingPhoto,
                modifier = Modifier.weight(1f)
            )
        }

        state.photoError?.let { message ->
            AlertBanner(message = message)
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

private const val MESSAGE_DURATION_MILLIS = 4_000L

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
            onChangePhoto = {},
            onFullNameChange = {},
            onBirthDateChange = {},
            onPhoneNumberChange = {},
            onSave = {}
        )
    }
}
