package com.vitalcare.serenia.features.socialcompanionship.presentation.tellday

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystem.components.SereniaBackground
import com.vitalcare.serenia.core.designsystem.components.SereniaPrimaryButton
import com.vitalcare.serenia.core.designsystem.components.SereniaSecondaryButton
import com.vitalcare.serenia.core.designsystem.icon.chevronLeft
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.features.socialcompanionship.domain.RecordingState
import com.vitalcare.serenia.features.socialcompanionship.presentation.tellday.components.TalkButton

@Composable
fun TellDayScreen(
    modifier: Modifier = Modifier,
    viewModel: TellDayViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onAudioSent: () -> Unit,
    onDiscard: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    TellDayContent(
        uiState = uiState,
        onBackClick = onBack,
        onTalkClick = viewModel::toggleRecording,
        onSendClick = {
            viewModel.sendAudio()
            onAudioSent()
        },
        onDiscardClick = {
            viewModel.discard()
            onDiscard()
        },
        modifier = modifier
    )
}

@Composable
fun TellDayContent(
    uiState: TellDayUiState,
    onBackClick: () -> Unit,
    onTalkClick: () -> Unit,
    onSendClick: () -> Unit,
    onDiscardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isRecording = uiState.recordingState == RecordingState.RECORDING

    SereniaBackground(modifier = modifier) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            // The min height keeps the buttons at the bottom while still allowing scroll on small screens
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = maxHeight)
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    TextButton(
                        onClick = onBackClick,
                        modifier = Modifier.heightIn(min = 56.dp),
                        contentPadding = PaddingValues(start = 0.dp, end = 12.dp)
                    ) {
                        Icon(
                            imageVector = chevronLeft,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Atrás", style = MaterialTheme.typography.titleLarge)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Cuéntale cómo estuvo tu día",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Toca el botón y habla con calma. Se envía a tu familia vinculada.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                TalkButton(
                    text = if (isRecording) "Parar" else "Hablar",
                    isRecording = isRecording,
                    onClick = onTalkClick,
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = when (uiState.recordingState) {
                            RecordingState.IDLE -> "Toca para grabar"
                            RecordingState.RECORDING -> "Grabando ${uiState.formattedTime}"
                            RecordingState.RECORDED -> "Listo - ${uiState.formattedTime}"
                        },
                        style = MaterialTheme.typography.labelLarge,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SereniaPrimaryButton(
                        text = "Enviar a mi familia",
                        onClick = onSendClick,
                        enabled = uiState.canSend,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    SereniaSecondaryButton(
                        text = "Cancelar y descartar",
                        onClick = onDiscardClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TellDayScreenPreview() {
    SereniaTheme {
        TellDayContent(
            uiState = TellDayUiState(),
            onBackClick = {},
            onTalkClick = {},
            onSendClick = {},
            onDiscardClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TellDayScreenRecordedPreview() {
    SereniaTheme {
        TellDayContent(
            uiState = TellDayUiState(recordingState = RecordingState.RECORDED, elapsedSeconds = 12),
            onBackClick = {},
            onTalkClick = {},
            onSendClick = {},
            onDiscardClick = {}
        )
    }
}
