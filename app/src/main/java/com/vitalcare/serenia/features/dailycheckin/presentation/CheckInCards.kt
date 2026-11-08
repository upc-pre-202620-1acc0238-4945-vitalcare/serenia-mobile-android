package com.vitalcare.serenia.features.dailycheckin.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.bedtime
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaContrast
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystems.theme.SereniaTouch

@Composable
fun QuestionCard(
    question: String,
    onAnswer: (MoodAnswer) -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
    simplified: Boolean = false,
    enabled: Boolean = true
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        if (simplified) {
            QuestionContent(question = question, large = true, enabled = enabled, onAnswer = onAnswer)
        } else {
            val shape = RoundedCornerShape(SereniaRadius.xl)
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        shape = shape,
                        ambientColor = SereniaPalette.Teal700.copy(alpha = 0.16f),
                        spotColor = SereniaPalette.Teal700.copy(alpha = 0.16f)
                    )
                    .background(SereniaPalette.Neutro50, shape)
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                QuestionContent(question = question, large = false, enabled = enabled, onAnswer = onAnswer)
            }
        }

        SkipLink(enabled = enabled, onClick = onSkip)
    }
}

@Composable
private fun QuestionContent(
    question: String,
    large: Boolean,
    enabled: Boolean,
    onAnswer: (MoodAnswer) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = question,
            style = MaterialTheme.typography.headlineMedium,
            color = SereniaPalette.Neutro800,
            modifier = Modifier.semantics { heading() }
        )
        MoodAnswer.entries.forEach { answer ->
            MoodOption(
                answer = answer,
                large = large,
                enabled = enabled,
                onClick = { onAnswer(answer) }
            )
        }
    }
}

@Composable
private fun SkipLink(enabled: Boolean, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = SereniaTouch.senior)
            .clip(RoundedCornerShape(SereniaRadius.sm))
            .clickable(enabled = enabled, role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp)
    ) {
        Icon(
            imageVector = bedtime,
            contentDescription = null,
            tint = SereniaPalette.Teal700,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = "Hoy prefiero no responder",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = SereniaPalette.Teal700,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun AnsweredCard(
    firstName: String?,
    answer: MoodAnswer,
    modifier: Modifier = Modifier,
    simplified: Boolean = false
) {
    val title = if (firstName.isNullOrBlank()) "Gracias" else "Gracias, $firstName"

    if (simplified) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .background(answer.answeredBackground, RoundedCornerShape(SereniaRadius.xl))
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            IconCircle(icon = answer.icon, background = answer.circle, tint = answer.face, size = 88.dp, iconSize = 56.dp)
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = answer.face,
                textAlign = TextAlign.Center
            )
            Text(
                text = answer.answeredMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = SereniaPalette.Neutro700,
                textAlign = TextAlign.Center
            )
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(answer.answeredBackground, RoundedCornerShape(SereniaRadius.lg))
                .padding(start = 16.dp, top = 16.dp, end = 20.dp, bottom = 16.dp)
        ) {
            IconCircle(icon = answer.icon, background = answer.circle, tint = answer.face, size = 56.dp, iconSize = 36.dp)
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = title,
                    fontFamily = Nunito,
                    fontSize = 20.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = answer.face
                )
                Text(
                    text = answer.answeredMessage,
                    fontFamily = Nunito,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = SereniaPalette.Neutro700
                )
            }
        }
    }
}

@Composable
fun PauseCard(
    onResume: () -> Unit,
    modifier: Modifier = Modifier,
    simplified: Boolean = false,
    isLoading: Boolean = false
) {
    val title = "Hoy no te preguntamos nada"
    val body = "Aquí estamos cuando quieras. Tu familia sabe que hoy elegiste una pausa."
    val shape = RoundedCornerShape(SereniaRadius.xl)
    val cardModifier = modifier
        .fillMaxWidth()
        .background(SereniaPalette.Durazno100, shape)
        .border(BorderStroke(SereniaContrast.stroke(2.dp), SereniaContrast.border(SereniaPalette.Durazno300)), shape)

    if (simplified) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = cardModifier.padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            IconCircle(icon = bedtime, background = SereniaPalette.Durazno200, tint = SereniaPalette.Durazno900, size = 88.dp, iconSize = 52.dp)
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Durazno900,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge,
                color = SereniaPalette.Neutro700,
                textAlign = TextAlign.Center
            )
            PrimaryButton(
                text = "Retomar mis preguntas",
                onClick = onResume,
                isLoading = isLoading,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = cardModifier.padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconCircle(icon = bedtime, background = SereniaPalette.Durazno200, tint = SereniaPalette.Durazno900, size = 56.dp, iconSize = 32.dp)
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = SereniaPalette.Neutro800,
                    modifier = Modifier.semantics { heading() }
                )
            }
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = SereniaPalette.Neutro700
            )
            PrimaryButton(
                text = "Retomar mis preguntas",
                onClick = onResume,
                isLoading = isLoading
            )
        }
    }
}

@Composable
fun CheckInMessageCard(
    title: String,
    body: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: () -> Unit = {}
) {
    val shape = RoundedCornerShape(SereniaRadius.xl)

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .background(SereniaPalette.Neutro50, shape)
            .border(BorderStroke(SereniaContrast.stroke(2.dp), SereniaContrast.border(SereniaPalette.Neutro200)), shape)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = SereniaPalette.Neutro800,
            modifier = Modifier.semantics { heading() }
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = SereniaPalette.Neutro700
        )
        if (actionLabel != null) {
            PrimaryButton(
                text = actionLabel,
                onClick = onAction,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
private fun IconCircle(
    icon: ImageVector,
    background: Color,
    tint: Color,
    size: Dp,
    iconSize: Dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .background(background, CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun CheckInCardsPreview() {
    SereniaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            QuestionCard(question = "¿Cómo amaneciste hoy?", onAnswer = {}, onSkip = {})
            AnsweredCard(firstName = "Rosa", answer = MoodAnswer.GOOD)
            PauseCard(onResume = {})
        }
    }
}
