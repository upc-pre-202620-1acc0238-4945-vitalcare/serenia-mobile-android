package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.check
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.SecondaryButton
import com.vitalcare.serenia.core.designsystems.components.TextLinkButton
import com.vitalcare.serenia.core.designsystems.phone
import com.vitalcare.serenia.core.designsystems.place
import com.vitalcare.serenia.core.designsystems.schedule
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaContrast
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystems.theme.SereniaTouch
import com.vitalcare.serenia.core.format.formatRelativeDay
import com.vitalcare.serenia.core.format.formatTime
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderDateTime
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminder
import com.vitalcare.serenia.features.socialcompanionship.domain.SocialReminderStatus

@Composable
fun DueReminderCard(
    reminder: SocialReminder,
    isBusy: Boolean,
    errorMessage: String?,
    onDone: () -> Unit,
    onLater: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    HighlightedReminderCard(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            ReminderIcon(reminder = reminder)
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Es hora",
                    fontFamily = Nunito,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SereniaPalette.Neutro50,
                    modifier = Modifier
                        .background(SereniaPalette.Durazno800, CircleShape)
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                )
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = SereniaPalette.Neutro800,
                    modifier = Modifier.semantics { heading() }
                )
                Text(
                    text = reminder.remindAt.toDisplayText(),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SereniaPalette.Neutro700
                )
            }
        }

        errorMessage?.let { message ->
            AlertBanner(message = message)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = onDone,
                enabled = !isBusy,
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SereniaPalette.Verde800,
                    contentColor = SereniaPalette.Neutro50,
                    disabledContainerColor = SereniaPalette.Verde800,
                    disabledContentColor = SereniaPalette.Neutro50
                ),
                contentPadding = CardButtonPadding,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = SereniaTouch.seniorPrimary)
            ) {
                Icon(
                    imageVector = check,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .size(22.dp)
                )
                CardButtonText(text = "Ya lo hice")
            }
            OutlinedButton(
                onClick = onLater,
                enabled = !isBusy,
                shape = CircleShape,
                border = BorderStroke(SereniaContrast.stroke(2.dp), SereniaContrast.border(SereniaPalette.Durazno700)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = SereniaPalette.Neutro50,
                    contentColor = SereniaPalette.Durazno800,
                    disabledContainerColor = SereniaPalette.Neutro50,
                    disabledContentColor = SereniaPalette.Durazno800
                ),
                contentPadding = CardButtonPadding,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = SereniaTouch.seniorPrimary)
            ) {
                if (isBusy) {
                    CircularProgressIndicator(
                        color = SereniaPalette.Durazno800,
                        strokeWidth = 3.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    CardButtonText(text = "Más tarde")
                }
            }
        }

        TextLinkButton(
            text = "Ya no lo necesito",
            onClick = onCancel,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun ReminderCard(
    reminder: SocialReminder,
    errorMessage: String?,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                shape = shape,
                ambientColor = SereniaPalette.Teal700.copy(alpha = 0.08f),
                spotColor = SereniaPalette.Teal700.copy(alpha = 0.08f)
            )
            .background(SereniaPalette.Neutro50, shape)
            .padding(start = 20.dp, top = 16.dp, end = 20.dp, bottom = 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ReminderIcon(reminder = reminder)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = SereniaPalette.Neutro800,
                    modifier = Modifier.semantics { heading() }
                )
                if (reminder.isPostponed) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = schedule,
                            contentDescription = "Pospuesto",
                            tint = SereniaPalette.Durazno800,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = reminder.remindAt.toDisplayText(),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = SereniaPalette.Durazno800
                        )
                    }
                } else {
                    Text(
                        text = reminder.remindAt.toDisplayText(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = SereniaPalette.Neutro600
                    )
                }
            }
        }

        errorMessage?.let { message ->
            AlertBanner(message = message, modifier = Modifier.padding(top = 8.dp))
        }

        TextLinkButton(
            text = "Ya no lo necesito",
            onClick = onCancel,
            modifier = Modifier.padding(start = 68.dp)
        )
    }
}

@Composable
fun CancelReminderCard(
    reminder: SocialReminder,
    isCanceling: Boolean,
    errorMessage: String?,
    onConfirm: () -> Unit,
    onKeep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(Unit) {
        bringIntoViewRequester.bringIntoView()
    }

    HighlightedReminderCard(modifier = modifier.bringIntoViewRequester(bringIntoViewRequester)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            ReminderIcon(reminder = reminder)
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "¿Ya no necesitas este recordatorio?",
                    style = MaterialTheme.typography.headlineSmall,
                    color = SereniaPalette.Neutro800,
                    modifier = Modifier.semantics { heading() }
                )
                Text(
                    text = "${reminder.title} · ${reminder.remindAt.toDisplayText()}. No te lo volveremos a mostrar.",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = SereniaPalette.Neutro700
                )
            }
        }

        errorMessage?.let { message ->
            AlertBanner(message = message)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            PrimaryButton(
                text = "Sí, ya no lo necesito",
                onClick = onConfirm,
                isLoading = isCanceling,
                elevated = false
            )
            SecondaryButton(
                text = "No, mantenerlo",
                onClick = onKeep,
                enabled = !isCanceling,
                color = SereniaPalette.Durazno800
            )
        }
    }
}

@Composable
private fun HighlightedReminderCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = shape,
                ambientColor = SereniaPalette.Teal700.copy(alpha = 0.16f),
                spotColor = SereniaPalette.Teal700.copy(alpha = 0.16f)
            )
            .background(SereniaPalette.Durazno100, shape)
            .border(BorderStroke(SereniaContrast.stroke(2.dp), SereniaContrast.border(SereniaPalette.Durazno400)), shape)
            .padding(start = 20.dp, top = 20.dp, end = 20.dp, bottom = 8.dp),
        content = content
    )
}

@Composable
private fun ReminderIcon(reminder: SocialReminder) {
    val background = if (reminder.isCall) SereniaPalette.Verde200 else SereniaPalette.Durazno200
    val tint = if (reminder.isCall) SereniaPalette.Verde900 else SereniaPalette.Durazno900

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(56.dp)
            .background(background, CircleShape)
    ) {
        Icon(
            imageVector = if (reminder.isCall) phone else place,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun CardButtonText(text: String) {
    Text(
        text = text,
        fontFamily = Nunito,
        fontSize = 20.sp,
        lineHeight = 24.sp,
        fontWeight = FontWeight.ExtraBold
    )
}

private val CardButtonPadding = PaddingValues(horizontal = 8.dp, vertical = 12.dp)

fun ReminderDateTime.toDisplayText(): String {
    return "${formatRelativeDay(year, month, day)}, ${formatTime(hour, minute)}"
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun ReminderCardsPreview() {
    val now = ReminderDateTime.now()
    val call = SocialReminder(
        id = "1",
        title = "Llamar a doña Carmen",
        remindAt = now,
        status = SocialReminderStatus.PRESENTED
    )
    val market = SocialReminder(
        id = "2",
        title = "Ir al mercado",
        remindAt = now,
        status = SocialReminderStatus.POSTPONED
    )

    SereniaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            DueReminderCard(
                reminder = call,
                isBusy = false,
                errorMessage = null,
                onDone = {},
                onLater = {},
                onCancel = {}
            )
            ReminderCard(reminder = market, errorMessage = null, onCancel = {})
            CancelReminderCard(
                reminder = call,
                isCanceling = false,
                errorMessage = null,
                onConfirm = {},
                onKeep = {}
            )
        }
    }
}
