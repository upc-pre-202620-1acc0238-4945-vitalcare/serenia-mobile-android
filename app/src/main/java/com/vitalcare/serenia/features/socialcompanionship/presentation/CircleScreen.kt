package com.vitalcare.serenia.features.socialcompanionship.presentation.circle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import com.vitalcare.serenia.core.designsystem.components.SereniaBackground
import com.vitalcare.serenia.core.designsystem.components.SereniaSnackbar
import com.vitalcare.serenia.core.designsystem.icon.chevronLeft
import com.vitalcare.serenia.core.designsystem.icon.contentCopy
import com.vitalcare.serenia.core.designsystem.icon.refresh
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.features.socialcompanionship.domain.CircleMember
import com.vitalcare.serenia.features.socialcompanionship.presentation.circle.components.CircleMemberCard
import com.vitalcare.serenia.features.socialcompanionship.presentation.circle.components.CodeActionButton
import com.vitalcare.serenia.features.socialcompanionship.presentation.circle.components.InvitationCodeCard

@Composable
fun CircleScreen(
    modifier: Modifier = Modifier,
    viewModel: CircleViewModel = hiltViewModel(),
    onBack: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            val message = when (event) {
                CircleEvent.CodeCopied -> "Código copiado"
                CircleEvent.CodeRenewed -> "Generamos un código nuevo"
                is CircleEvent.MemberRemoved -> "${event.name} ya no tiene acceso"
            }
            // The message runs on its own so it never holds back the next event
            launch {
                snackbarHostState.currentSnackbarData?.dismiss()
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    CircleContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onBack,
        onCopyClick = viewModel::copyCode,
        onRenewClick = viewModel::renewCode,
        onRemoveClick = viewModel::askToRemove,
        onConfirmRemoval = viewModel::confirmRemoval,
        onCancelRemoval = viewModel::cancelRemoval,
        modifier = modifier
    )
}

@Composable
fun CircleContent(
    uiState: CircleUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onCopyClick: () -> Unit,
    onRenewClick: () -> Unit,
    onRemoveClick: (Int) -> Unit,
    onConfirmRemoval: () -> Unit,
    onCancelRemoval: () -> Unit,
    modifier: Modifier = Modifier
) {
    val avatarColors = listOf(
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.primaryContainer
    )

    SereniaBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
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
                text = "Mi círculo",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            InvitationCodeCard(code = uiState.invitationCode)

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CodeActionButton(
                    text = "Copiar",
                    icon = contentCopy,
                    onClick = onCopyClick,
                    modifier = Modifier.weight(1f)
                )
                CodeActionButton(
                    text = "Código nuevo",
                    icon = refresh,
                    onClick = onRenewClick,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Quién me acompaña",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                uiState.members.forEach { member ->
                    CircleMemberCard(
                        member = member,
                        // The color follows the member so it does not change when someone else is removed
                        avatarColor = avatarColors[(member.id - 1) % avatarColors.size],
                        isConfirmingRemoval = uiState.memberPendingRemovalId == member.id,
                        onRemoveClick = { onRemoveClick(member.id) },
                        onConfirmRemoval = onConfirmRemoval,
                        onCancelRemoval = onCancelRemoval
                    )
                }
            }

            // Leaves room so the message never covers the last member
            Spacer(modifier = Modifier.height(72.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) { snackbarData ->
            SereniaSnackbar(message = snackbarData.visuals.message)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CircleScreenPreview() {
    SereniaTheme {
        CircleContent(
            uiState = CircleUiState(
                invitationCode = "4821",
                members = listOf(
                    CircleMember(id = 1, name = "Lucía", relationship = "Tu hija", initials = "LM"),
                    CircleMember(id = 2, name = "Martín", relationship = "Tu nieto", initials = "MR")
                )
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onCopyClick = {},
            onRenewClick = {},
            onRemoveClick = {},
            onConfirmRemoval = {},
            onCancelRemoval = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CircleScreenConfirmingPreview() {
    SereniaTheme {
        CircleContent(
            uiState = CircleUiState(
                invitationCode = "4821",
                members = listOf(
                    CircleMember(id = 1, name = "Lucía", relationship = "Tu hija", initials = "LM"),
                    CircleMember(id = 2, name = "Martín", relationship = "Tu nieto", initials = "MR")
                ),
                memberPendingRemovalId = 1
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onCopyClick = {},
            onRenewClick = {},
            onRemoveClick = {},
            onConfirmRemoval = {},
            onCancelRemoval = {}
        )
    }
}
