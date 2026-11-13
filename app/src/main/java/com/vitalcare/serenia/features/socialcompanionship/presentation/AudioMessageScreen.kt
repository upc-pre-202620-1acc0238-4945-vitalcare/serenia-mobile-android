package com.vitalcare.serenia.features.socialcompanionship.presentation

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.BackLink
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.SecondaryButton
import com.vitalcare.serenia.core.designsystems.components.TextLinkButton
import com.vitalcare.serenia.core.designsystems.mic
import com.vitalcare.serenia.core.designsystems.pause
import com.vitalcare.serenia.core.designsystems.playArrow
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaContrast
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.format.formatDuration
import com.vitalcare.serenia.core.format.joinNames

enum class MicrophoneStep {
    NONE,
    EXPLAIN,
    DENIED
}

@Composable
fun AudioMessageScreen(
    onSent: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AudioMessageViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var microphoneStep by rememberSaveable { mutableStateOf(MicrophoneStep.NONE) }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            microphoneStep = MicrophoneStep.NONE
            viewModel.startRecording()
        } else {
            microphoneStep = MicrophoneStep.DENIED
        }
    }

    LifecycleResumeEffect(Unit) {
        if (microphoneStep == MicrophoneStep.DENIED && context.hasMicrophonePermission()) {
            microphoneStep = MicrophoneStep.NONE
        }
        onPauseOrDispose { }
    }

    LaunchedEffect(state.isSent, state.isClosed) {
        when {
            state.isSent -> {
                viewModel.onExitHandled()
                onSent()
            }

            state.isClosed -> {
                viewModel.onExitHandled()
                onClose()
            }
        }
    }

    val leave = {
        when (state.phase) {
            AudioPhase.RECORDING -> viewModel.cancelRecording()
            AudioPhase.REVIEW -> viewModel.discard()
            AudioPhase.READY -> onClose()
        }
    }
    BackHandler(onBack = leave)

    AudioMessageContent(
        state = state,
        microphoneStep = microphoneStep,
        modifier = modifier,
        onBack = leave,
        onRecord = {
            if (context.hasMicrophonePermission()) {
                viewModel.startRecording()
            } else {
                microphoneStep = MicrophoneStep.EXPLAIN
            }
        },
        onAllowMicrophone = { permissionLauncher.launch(Manifest.permission.RECORD_AUDIO) },
        onOpenSettings = { context.openAppSettings() },
        onStop = viewModel::stopRecording,
        onCancelRecording = viewModel::cancelRecording,
        onTogglePlayback = viewModel::togglePlayback,
        onSend = viewModel::send,
        onRecordAgain = viewModel::recordAgain,
        onDiscard = viewModel::discard
    )
}

@Composable
fun AudioMessageContent(
    state: AudioMessageUiState,
    microphoneStep: MicrophoneStep,
    onBack: () -> Unit,
    onRecord: () -> Unit,
    onAllowMicrophone: () -> Unit,
    onOpenSettings: () -> Unit,
    onStop: () -> Unit,
    onCancelRecording: () -> Unit,
    onTogglePlayback: () -> Unit,
    onSend: () -> Unit,
    onRecordAgain: () -> Unit,
    onDiscard: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
    ) {
        val screenHeight = maxHeight

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .heightIn(min = screenHeight)
                .padding(start = 24.dp, top = 12.dp, end = 24.dp, bottom = 16.dp)
        ) {
            Column {
                BackLink(onClick = onBack, text = "Mi familia")
                Text(
                    text = "Cuéntame algo",
                    style = MaterialTheme.typography.headlineLarge,
                    color = SereniaPalette.Teal700,
                    modifier = Modifier.semantics { heading() }
                )
            }

            when (state.phase) {
                AudioPhase.READY -> ReadyContent(
                    error = state.error,
                    microphoneStep = microphoneStep,
                    onRecord = onRecord,
                    onAllowMicrophone = onAllowMicrophone,
                    onOpenSettings = onOpenSettings
                )

                AudioPhase.RECORDING -> RecordingContent(
                    state = state,
                    onStop = onStop,
                    onCancel = onCancelRecording
                )

                AudioPhase.REVIEW -> ReviewContent(
                    state = state,
                    onTogglePlayback = onTogglePlayback,
                    onSend = onSend,
                    onRecordAgain = onRecordAgain,
                    onDiscard = onDiscard
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.ReadyContent(
    error: String?,
    microphoneStep: MicrophoneStep,
    onRecord: () -> Unit,
    onAllowMicrophone: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Text(
        text = "Cuéntale a tu familia cómo estuvo tu día. Toca el botón y habla con calma.",
        style = MaterialTheme.typography.bodyMedium,
        color = SereniaPalette.Neutro700
    )

    error?.let { message ->
        AlertBanner(message = message)
    }

    when (microphoneStep) {
        MicrophoneStep.EXPLAIN -> MicrophoneCard(
            title = "Necesitamos tu micrófono",
            body = "Para grabar tu voz, el teléfono te pedirá permiso para usar el micrófono. " +
                "Solo lo usamos mientras grabas.",
            actionLabel = "Permitir el micrófono",
            onAction = onAllowMicrophone
        )

        MicrophoneStep.DENIED -> MicrophoneCard(
            title = "No podemos grabar sin el micrófono",
            body = "Puedes activarlo en los ajustes del teléfono, en Permisos, y volver aquí.",
            actionLabel = "Abrir ajustes",
            onAction = onOpenSettings
        )

        MicrophoneStep.NONE -> Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(180.dp)
                    .shadow(elevation = 12.dp, shape = CircleShape)
                    .background(SereniaPalette.Durazno200, CircleShape)
                    .padding(14.dp)
                    .clip(CircleShape)
                    .background(SereniaPalette.Durazno700, CircleShape)
                    .clickable(role = Role.Button, onClick = onRecord)
                    .clearAndSetSemantics { contentDescription = "Empezar a grabar" }
            ) {
                Icon(
                    imageVector = mic,
                    contentDescription = null,
                    tint = SereniaPalette.Neutro50,
                    modifier = Modifier.size(72.dp)
                )
            }
            Text(
                text = "Toca para grabar",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Neutro800,
                modifier = Modifier.padding(top = 18.dp)
            )
            Text(
                text = "Hasta 2 minutos",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.SemiBold,
                color = SereniaPalette.Neutro700
            )
        }
    }
}

@Composable
private fun MicrophoneCard(
    title: String,
    body: String,
    actionLabel: String,
    onAction: () -> Unit
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
            .background(SereniaPalette.Durazno100, shape)
            .border(BorderStroke(SereniaContrast.stroke(2.dp), SereniaContrast.border(SereniaPalette.Durazno300)), shape)
            .padding(20.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .background(SereniaPalette.Durazno700, CircleShape)
            ) {
                Icon(
                    imageVector = mic,
                    contentDescription = null,
                    tint = SereniaPalette.Neutro50,
                    modifier = Modifier.size(30.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Neutro800,
                modifier = Modifier
                    .weight(1f)
                    .semantics { heading() }
            )
        }
        Text(
            text = body,
            style = MaterialTheme.typography.bodySmall,
            color = SereniaPalette.Neutro700
        )
        PrimaryButton(text = actionLabel, onClick = onAction, elevated = false)
    }
}

@Composable
private fun ColumnScope.RecordingContent(
    state: AudioMessageUiState,
    onStop: () -> Unit,
    onCancel: () -> Unit
) {
    Text(
        text = "Te estamos escuchando…",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = SereniaPalette.Durazno800,
        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = formatDuration(state.elapsedSeconds),
            fontFamily = Nunito,
            fontSize = 48.sp,
            lineHeight = 56.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SereniaPalette.Neutro800
        )
        Waveform(levels = state.levels)
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 16.dp)
                .size(180.dp)
                .background(SereniaPalette.Durazno100, CircleShape)
                .padding(12.dp)
                .background(SereniaPalette.Durazno200, CircleShape)
                .padding(12.dp)
                .clip(CircleShape)
                .background(SereniaPalette.Durazno800, CircleShape)
                .clickable(role = Role.Button, onClick = onStop)
                .clearAndSetSemantics { contentDescription = "Terminar grabación" }
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(SereniaPalette.Neutro50, RoundedCornerShape(8.dp))
            )
        }
        Text(
            text = "Toca para terminar",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = SereniaPalette.Neutro800,
            modifier = Modifier.padding(top = 8.dp)
        )
    }

    Spacer(modifier = Modifier.weight(1f))

    TextLinkButton(
        text = "Cancelar y no enviar",
        onClick = onCancel,
        modifier = Modifier.align(Alignment.CenterHorizontally)
    )
}

@Composable
private fun Waveform(levels: List<Float>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.height(64.dp)
    ) {
        repeat(WAVE_BARS) { index ->
            val level = levels.getOrNull(index)
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(if (level != null) (MIN_BAR + (MAX_BAR - MIN_BAR) * level).dp else MIN_BAR.dp)
                    .background(
                        if (level != null) SereniaPalette.Durazno700 else SereniaPalette.Neutro300,
                        RoundedCornerShape(3.dp)
                    )
            )
        }
    }
}

@Composable
private fun ColumnScope.ReviewContent(
    state: AudioMessageUiState,
    onTogglePlayback: () -> Unit,
    onSend: () -> Unit,
    onRecordAgain: () -> Unit,
    onDiscard: () -> Unit
) {
    Text(
        text = "Escúchalo si quieres antes de enviarlo.",
        style = MaterialTheme.typography.bodyMedium,
        color = SereniaPalette.Neutro700
    )

    AudioPlayerCard(
        seconds = state.recordedSeconds,
        isPlaying = state.isPlaying,
        progress = state.playbackProgress,
        onToggle = onTogglePlayback,
        modifier = Modifier.padding(top = 12.dp)
    )

    state.recipients?.let { recipients ->
        Text(
            text = if (recipients.isEmpty()) {
                "Todavía no hay familiares en tu círculo para recibirlo."
            } else {
                "Lo recibirán ${joinNames(recipients)}."
            },
            style = MaterialTheme.typography.bodySmall,
            color = SereniaPalette.Neutro700
        )
    }

    Spacer(modifier = Modifier.weight(1f))

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(top = 24.dp)
    ) {
        state.error?.let { message ->
            AlertBanner(message = message)
        }
        PrimaryButton(
            text = "Enviar a mi familia",
            onClick = onSend,
            enabled = state.canSend,
            isLoading = state.isSending
        )
        SecondaryButton(
            text = "Grabar de nuevo",
            onClick = onRecordAgain,
            enabled = !state.isSending,
            color = SereniaPalette.Durazno700
        )
        TextLinkButton(
            text = "No enviar",
            onClick = onDiscard,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun AudioPlayerCard(
    seconds: Int,
    isPlaying: Boolean,
    progress: Float,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = shape,
                ambientColor = SereniaPalette.Teal700.copy(alpha = 0.08f),
                spotColor = SereniaPalette.Teal700.copy(alpha = 0.08f)
            )
            .background(SereniaPalette.Neutro50, shape)
            .padding(20.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(SereniaPalette.Teal700, CircleShape)
                .clickable(role = Role.Button, onClick = onToggle)
                .clearAndSetSemantics {
                    contentDescription = if (isPlaying) "Pausar tu audio" else "Escuchar tu audio"
                }
        ) {
            Icon(
                imageVector = if (isPlaying) pause else playArrow,
                contentDescription = null,
                tint = SereniaPalette.Neutro50,
                modifier = Modifier.size(36.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Tu audio · ${formatDuration(seconds)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Neutro800
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(SereniaPalette.Neutro200, CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(SereniaPalette.Teal700, CircleShape)
                )
            }
        }
    }
}

private fun Context.hasMicrophonePermission(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
}

private fun Context.openAppSettings() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", packageName, null))
        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}

private const val WAVE_BARS = 26
private const val MIN_BAR = 10f
private const val MAX_BAR = 64f

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun AudioReadyContentPreview() {
    SereniaTheme {
        AudioMessageContent(
            state = AudioMessageUiState(),
            microphoneStep = MicrophoneStep.NONE,
            onBack = {}, onRecord = {}, onAllowMicrophone = {}, onOpenSettings = {}, onStop = {},
            onCancelRecording = {}, onTogglePlayback = {}, onSend = {}, onRecordAgain = {}, onDiscard = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun AudioRecordingContentPreview() {
    SereniaTheme {
        AudioMessageContent(
            state = AudioMessageUiState(
                phase = AudioPhase.RECORDING,
                elapsedSeconds = 42,
                levels = List(18) { index -> (index % 5) / 4f }
            ),
            microphoneStep = MicrophoneStep.NONE,
            onBack = {}, onRecord = {}, onAllowMicrophone = {}, onOpenSettings = {}, onStop = {},
            onCancelRecording = {}, onTogglePlayback = {}, onSend = {}, onRecordAgain = {}, onDiscard = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun AudioReviewContentPreview() {
    SereniaTheme {
        AudioMessageContent(
            state = AudioMessageUiState(
                phase = AudioPhase.REVIEW,
                recordedSeconds = 42,
                recipients = listOf("Lucía", "Martín")
            ),
            microphoneStep = MicrophoneStep.NONE,
            onBack = {}, onRecord = {}, onAllowMicrophone = {}, onOpenSettings = {}, onStop = {},
            onCancelRecording = {}, onTogglePlayback = {}, onSend = {}, onRecordAgain = {}, onDiscard = {}
        )
    }
}
