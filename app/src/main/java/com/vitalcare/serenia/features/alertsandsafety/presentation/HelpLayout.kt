package com.vitalcare.serenia.features.alertsandsafety.presentation

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.notifications
import com.vitalcare.serenia.core.designsystems.notificationsActive
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystems.theme.SereniaTouch
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HelpLayout(
    onHelpSent: (HelpOutcome) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HelpViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.outcome) {
        state.outcome?.let { outcome ->
            viewModel.onOutcomeShown()
            onHelpSent(outcome)
        }
    }

    HelpLayoutContent(
        onConfirmed = viewModel::sendHelpRequest,
        modifier = modifier,
        content = content
    )
}

@Composable
fun HelpLayoutContent(
    onConfirmed: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var isHolding by remember { mutableStateOf(false) }
    var isConfirmed by remember { mutableStateOf(false) }
    var isHintVisible by remember { mutableStateOf(false) }
    var secondsLeft by remember { mutableIntStateOf(HOLD_SECONDS) }
    val progress = remember { Animatable(0f) }
    val vibrate = rememberVibration()
    val currentOnConfirmed by rememberUpdatedState(onConfirmed)

    LaunchedEffect(isHolding) {
        if (!isHolding) {
            if (!isConfirmed) progress.snapTo(0f)
            return@LaunchedEffect
        }
        isHintVisible = false
        launch {
            progress.snapTo(0f)
            progress.animateTo(1f, tween(durationMillis = HOLD_SECONDS * 1_000, easing = LinearEasing))
        }
        for (second in HOLD_SECONDS downTo 1) {
            secondsLeft = second
            vibrate(SECOND_VIBRATION_MILLIS)
            delay(1_000)
        }
        isConfirmed = true
        vibrate(CONFIRMED_VIBRATION_MILLIS)
        currentOnConfirmed()
    }

    LaunchedEffect(isHintVisible) {
        if (isHintVisible) {
            delay(HINT_DURATION_MILLIS)
            isHintVisible = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (isHolding || isConfirmed) {
                    HelpCountdown(
                        secondsLeft = secondsLeft,
                        progress = progress.value,
                        isSending = isConfirmed
                    )
                } else {
                    content()
                }
            }

            HelpButton(
                isPressed = isHolding || isConfirmed,
                modifier = Modifier
                    .padding(start = SereniaSpacing.screen, top = 8.dp, end = SereniaSpacing.screen, bottom = 16.dp)
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onPress = {
                                if (isConfirmed) return@detectTapGestures
                                isHolding = true
                                tryAwaitRelease()
                                isHolding = false
                                if (!isConfirmed) {
                                    isHintVisible = true
                                    vibrate(SHORT_VIBRATION_MILLIS)
                                }
                            }
                        )
                    }
            )
        }

        if (isHintVisible) {
            HelpHint(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(start = SereniaSpacing.screen, end = SereniaSpacing.screen, bottom = HINT_BOTTOM_OFFSET)
            )
        }
    }
}

@Composable
fun HelpButton(
    isPressed: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = SereniaTouch.sos)
            .shadow(
                elevation = 6.dp,
                shape = CircleShape,
                ambientColor = SereniaPalette.Teal700.copy(alpha = 0.16f),
                spotColor = SereniaPalette.Teal700.copy(alpha = 0.16f)
            )
            .background(SereniaPalette.Error, CircleShape)
            .then(if (isPressed) Modifier.border(4.dp, SereniaPalette.Durazno900, CircleShape) else Modifier)
            .semantics {
                contentDescription = "Pedir ayuda. Mantén presionado 3 segundos para avisar a tu familia"
                role = Role.Button
            }
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        if (isPressed) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ButtonLabel(text = "Avisando a tu familia…")
                Text(
                    text = "No sueltes",
                    fontFamily = Nunito,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SereniaPalette.Neutro50
                )
            }
        } else {
            ButtonLabel(text = "Pedir ayuda")
        }
    }
}

@Composable
private fun ButtonLabel(text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = notifications,
            contentDescription = null,
            tint = SereniaPalette.Neutro50,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = SereniaPalette.Neutro50
        )
    }
}

@Composable
fun HelpCountdown(
    secondsLeft: Int,
    progress: Float,
    isSending: Boolean,
    modifier: Modifier = Modifier
) {
    val title = if (isSending) "Avisando a tu familia…" else "Sigue presionando"
    val body = if (isSending) "Espera un momento." else "Vamos a avisar a todos.\nSi sueltas, no se envía nada."
    val unit = if (secondsLeft == 1) "segundo" else "segundos"

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp, vertical = 8.dp)
            .semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Assertive }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(176.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 14.dp.toPx()
                val inset = strokeWidth / 2
                val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
                drawArc(
                    color = SereniaPalette.ErrorBg,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = arcSize,
                    style = Stroke(width = strokeWidth)
                )
                drawArc(
                    color = SereniaPalette.Error,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
            if (isSending) {
                Icon(
                    imageVector = notificationsActive,
                    contentDescription = null,
                    tint = SereniaPalette.Error,
                    modifier = Modifier.size(64.dp)
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = secondsLeft.toString(),
                        fontFamily = Nunito,
                        fontSize = 64.sp,
                        lineHeight = 64.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SereniaPalette.Error
                    )
                    Text(
                        text = unit,
                        fontFamily = Nunito,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = SereniaPalette.Neutro700
                    )
                }
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = SereniaPalette.Neutro800,
            textAlign = TextAlign.Center
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyLarge,
            color = SereniaPalette.Neutro700,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HelpHint(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(SereniaRadius.md)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) { liveRegion = LiveRegionMode.Polite }
    ) {
        Text(
            text = "Mantén presionado para avisar",
            fontFamily = Nunito,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SereniaPalette.Neutro50,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 16.dp,
                    shape = shape,
                    ambientColor = SereniaPalette.Teal700.copy(alpha = 0.22f),
                    spotColor = SereniaPalette.Teal700.copy(alpha = 0.22f)
                )
                .background(SereniaPalette.Neutro800, shape)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
        Canvas(modifier = Modifier.size(width = 24.dp, height = 12.dp)) {
            val path = Path().apply {
                moveTo(0f, 0f)
                lineTo(size.width, 0f)
                lineTo(size.width / 2, size.height)
                close()
            }
            drawPath(path = path, color = SereniaPalette.Neutro800)
        }
    }
}

@Composable
private fun rememberVibration(): (Long) -> Unit {
    val context = LocalContext.current
    return remember(context) {
        val vibrator = context.vibrator()
        val vibrate: (Long) -> Unit = { millis ->
            if (vibrator?.hasVibrator() == true) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(millis, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(millis)
                }
            }
        }
        vibrate
    }
}

private fun Context.vibrator(): Vibrator? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        getSystemService(VibratorManager::class.java)?.defaultVibrator
    } else {
        getSystemService(Vibrator::class.java)
    }
}

private const val HOLD_SECONDS = 3
private const val HINT_DURATION_MILLIS = 3_000L
private const val SHORT_VIBRATION_MILLIS = 60L
private const val SECOND_VIBRATION_MILLIS = 150L
private const val CONFIRMED_VIBRATION_MILLIS = 400L
private val HINT_BOTTOM_OFFSET = 104.dp

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun HelpCountdownPreview() {
    SereniaTheme {
        Column {
            HelpCountdown(secondsLeft = 1, progress = 0.66f, isSending = false, modifier = Modifier.weight(1f))
            HelpHint(modifier = Modifier.padding(horizontal = 20.dp))
            HelpButton(isPressed = true, modifier = Modifier.padding(20.dp))
        }
    }
}
