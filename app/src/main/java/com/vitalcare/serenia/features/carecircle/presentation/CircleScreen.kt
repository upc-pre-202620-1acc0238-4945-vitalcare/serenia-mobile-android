package com.vitalcare.serenia.features.carecircle.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.BackLink
import com.vitalcare.serenia.core.designsystems.components.SecondaryButton
import com.vitalcare.serenia.core.designsystems.refresh
import com.vitalcare.serenia.core.designsystems.share
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.features.carecircle.domain.InvitationCode

@Composable
fun CircleScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CircleViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

    CircleContent(
        state = state,
        modifier = modifier,
        onBack = onBack,
        onShare = { invitationCode -> shareInvitationCode(context, invitationCode) },
        onNewCode = viewModel::generateNewCode,
        onRetry = viewModel::refresh,
        onRemove = viewModel::askToRemove,
        onConfirmRemove = viewModel::confirmRemove,
        onCancelRemove = viewModel::cancelRemove
    )
}

@Composable
fun CircleContent(
    state: CircleUiState,
    onBack: () -> Unit,
    onShare: (InvitationCode) -> Unit,
    onNewCode: () -> Unit,
    onRetry: () -> Unit,
    onRemove: (String) -> Unit,
    onConfirmRemove: () -> Unit,
    onCancelRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
            .verticalScroll(rememberScrollState())
            .padding(start = SereniaSpacing.screen, top = 12.dp, end = SereniaSpacing.screen, bottom = 24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BackLink(onClick = onBack, text = "Mi familia")
            Text(
                text = "Mi círculo",
                style = MaterialTheme.typography.headlineLarge,
                color = SereniaPalette.Teal700,
                modifier = Modifier.semantics { heading() }
            )
        }

        InvitationCodeCard(label = "Tu código para invitar", invitationCode = state.invitationCode)

        state.codeError?.let { message ->
            AlertBanner(message = message)
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SecondaryButton(
                text = "Compartir",
                icon = share,
                enabled = state.invitationCode != null,
                onClick = { state.invitationCode?.let(onShare) },
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = "Código nuevo",
                icon = refresh,
                enabled = !state.isGeneratingCode,
                onClick = onNewCode,
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "Quién me acompaña",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = SereniaPalette.Neutro800,
            modifier = Modifier
                .padding(top = 8.dp)
                .semantics { heading() }
        )

        val members = state.members
        val membersError = state.membersError
        when {
            membersError != null && members == null -> {
                AlertBanner(message = membersError)
                SecondaryButton(text = "Intentar de nuevo", icon = refresh, onClick = onRetry)
            }

            members == null -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
            ) {
                CircularProgressIndicator(color = SereniaPalette.Teal700)
            }

            members.isEmpty() -> EmptyCircleCard()

            else -> members.forEachIndexed { position, member ->
                val tone = MemberTone.forPosition(position)
                if (member.familyLinkId == state.memberToRemove) {
                    RemoveMemberCard(
                        member = member,
                        tone = tone,
                        isRemoving = state.isRemoving,
                        errorMessage = state.removeError,
                        onConfirm = onConfirmRemove,
                        onCancel = onCancelRemove
                    )
                } else {
                    FamilyMemberCard(
                        member = member,
                        tone = tone,
                        onRemove = { onRemove(member.familyLinkId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyCircleCard() {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(SereniaPalette.Neutro50, shape)
            .border(BorderStroke(2.dp, SereniaPalette.Neutro200), shape)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Todavía no se ha unido nadie",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = SereniaPalette.Neutro800
        )
        Text(
            text = "Comparte tu código con tu familia. Cuando lo escriban en su app, aparecerán aquí.",
            style = MaterialTheme.typography.bodySmall,
            color = SereniaPalette.Neutro700
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun CircleContentPreview() {
    SereniaTheme {
        CircleContent(
            state = CircleUiState(
                invitationCode = InvitationCode(id = "1", code = "K7M4QX2P", expiresAtMillis = 1_778_250_600_000),
                members = listOf(
                    FamilyMember(familyLinkId = "1", fullName = "Lucía Mendoza", relationshipLabel = "hija"),
                    FamilyMember(familyLinkId = "2", fullName = "Martín Ríos", relationshipLabel = "nieto")
                )
            ),
            onBack = {},
            onShare = {},
            onNewCode = {},
            onRetry = {},
            onRemove = {},
            onConfirmRemove = {},
            onCancelRemove = {}
        )
    }
}
