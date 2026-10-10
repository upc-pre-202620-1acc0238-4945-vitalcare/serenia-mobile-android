package com.vitalcare.serenia.features.socialcompanionship.presentation.reminders.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystem.icon.close
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.features.socialcompanionship.domain.Reminder
import com.vitalcare.serenia.features.socialcompanionship.domain.ReminderStatus

@Composable
fun ReminderCard(
    reminder: Reminder,
    onDoneClick: () -> Unit,
    onPostponeClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(28.dp)
    // Only a pending reminder can be highlighted as due
    val isHighlighted = reminder.isDue && reminder.status == ReminderStatus.PENDING

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, shape)
            .then(
                if (isHighlighted) {
                    Modifier.border(2.dp, MaterialTheme.colorScheme.primary, shape)
                } else {
                    Modifier
                }
            )
            .padding(22.dp)
    ) {
        if (isHighlighted) {
            Text(
                text = "Es hora",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Text(
            text = reminder.title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = reminder.schedule,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        when (reminder.status) {
            ReminderStatus.PENDING -> ReminderActions(
                onDoneClick = onDoneClick,
                onPostponeClick = onPostponeClick,
                onCancelClick = onCancelClick
            )

            ReminderStatus.DONE -> ReminderStatusLabel(
                text = "Hecho",
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                contentColor = MaterialTheme.colorScheme.primary
            )

            ReminderStatus.POSTPONED -> ReminderStatusLabel(
                text = "Pospuesto: te lo recordamos más tarde",
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                contentColor = MaterialTheme.colorScheme.secondary
            )

            ReminderStatus.MISSED -> ReminderStatusLabel(
                text = "No completado: pasó el día sin atenderlo",
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ReminderActions(
    onDoneClick: () -> Unit,
    onPostponeClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val shape = RoundedCornerShape(22.dp)
    val contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = onDoneClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .heightIn(min = 64.dp),
            shape = shape,
            contentPadding = contentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(text = "Listo", style = MaterialTheme.typography.titleMedium)
        }

        Button(
            onClick = onPostponeClick,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .heightIn(min = 64.dp),
            shape = shape,
            contentPadding = contentPadding,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            )
        ) {
            Text(
                text = "Más\ntarde",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }

        OutlinedButton(
            onClick = onCancelClick,
            modifier = Modifier
                .width(68.dp)
                .fillMaxHeight()
                .heightIn(min = 64.dp),
            shape = shape,
            contentPadding = PaddingValues(0.dp),
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.error),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = close,
                contentDescription = "Cancelar recordatorio",
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
private fun ReminderStatusLabel(
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = contentColor,
        modifier = Modifier
            .background(containerColor, RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9)
@Composable
fun ReminderCardPreview() {
    SereniaTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ReminderStatus.entries.forEach { status ->
                ReminderCard(
                    reminder = Reminder(
                        id = status.ordinal,
                        title = "Llamar a doña Carmen",
                        schedule = "Hoy, 4:00 PM",
                        status = status,
                        isDue = true
                    ),
                    onDoneClick = {},
                    onPostponeClick = {},
                    onCancelClick = {}
                )
            }
        }
    }
}
