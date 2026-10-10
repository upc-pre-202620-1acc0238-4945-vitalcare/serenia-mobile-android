package com.vitalcare.serenia.features.socialcompanionship.presentation.circle.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.features.socialcompanionship.domain.CircleMember

@Composable
fun CircleMemberCard(
    member: CircleMember,
    avatarColor: Color,
    isConfirmingRemoval: Boolean,
    onRemoveClick: () -> Unit,
    onConfirmRemoval: () -> Unit,
    onCancelRemoval: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(28.dp))
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(avatarColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = member.initials,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = member.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = member.relationship,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            OutlinedButton(
                onClick = onRemoveClick,
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    // The visible text is short, so the screen reader also gets the member name
                    .semantics { contentDescription = "Quitar a ${member.name}" },
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.error),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = "Quitar", style = MaterialTheme.typography.labelLarge)
            }
        }

        if (isConfirmingRemoval) {
            Spacer(modifier = Modifier.height(12.dp))

            RemovalConfirmation(
                memberName = member.name,
                onConfirm = onConfirmRemoval,
                onCancel = onCancelRemoval
            )
        }
    }
}

@Composable
private fun RemovalConfirmation(
    memberName: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "¿Quitar a $memberName? Dejará de recibir tus alertas y de ver tu estado.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
            lineHeight = 23.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = onConfirm,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(text = "Sí, quitar", style = MaterialTheme.typography.labelLarge)
            }

            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 48.dp),
                shape = RoundedCornerShape(24.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(text = "Cancelar", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9)
@Composable
fun CircleMemberCardPreview() {
    SereniaTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            CircleMemberCard(
                member = CircleMember(id = 1, name = "Lucía", relationship = "Tu hija", initials = "LM"),
                avatarColor = MaterialTheme.colorScheme.secondaryContainer,
                isConfirmingRemoval = false,
                onRemoveClick = {},
                onConfirmRemoval = {},
                onCancelRemoval = {}
            )
            CircleMemberCard(
                member = CircleMember(id = 2, name = "Martín", relationship = "Tu nieto", initials = "MR"),
                avatarColor = MaterialTheme.colorScheme.primaryContainer,
                isConfirmingRemoval = true,
                onRemoveClick = {},
                onConfirmRemoval = {},
                onCancelRemoval = {}
            )
        }
    }
}
