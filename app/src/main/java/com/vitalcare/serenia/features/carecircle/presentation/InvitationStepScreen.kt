package com.vitalcare.serenia.features.carecircle.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.BackLink
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.SecondaryButton
import com.vitalcare.serenia.core.designsystems.components.StepIndicator
import com.vitalcare.serenia.core.designsystems.refresh
import com.vitalcare.serenia.core.designsystems.share
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.features.carecircle.domain.InvitationCode

@Composable
fun InvitationStepScreen(
    onBack: () -> Unit,
    onEnter: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InvitationStepViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    InvitationStepContent(
        state = state,
        modifier = modifier,
        onBack = onBack,
        onShare = { invitationCode -> shareInvitationCode(context, invitationCode) },
        onRetry = viewModel::loadInvitationCode,
        onEnter = onEnter
    )
}

@Composable
fun InvitationStepContent(
    state: InvitationStepUiState,
    onBack: () -> Unit,
    onShare: (InvitationCode) -> Unit,
    onRetry: () -> Unit,
    onEnter: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
    ) {
        val screenHeight = maxHeight

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .heightIn(min = screenHeight)
                .padding(start = SereniaSpacing.authScreen, top = 12.dp, end = SereniaSpacing.authScreen, bottom = 24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    BackLink(onClick = onBack)
                    StepIndicator(current = 3)
                    Text(
                        text = "Este es tu código para tu familia",
                        style = MaterialTheme.typography.headlineLarge,
                        color = SereniaPalette.Teal700,
                        modifier = Modifier.semantics { heading() }
                    )
                    Text(
                        text = "Compártelo con tu hija, hijo o nieto. Con él, su app y la tuya quedan conectadas. " +
                            "Tú decides quién entra.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SereniaPalette.Neutro700
                    )
                }

                val error = state.error
                if (error != null) {
                    AlertBanner(message = error)
                    SecondaryButton(text = "Intentar de nuevo", icon = refresh, onClick = onRetry)
                } else {
                    InvitationCodeCard(label = "Tu código", invitationCode = state.invitationCode)
                    SecondaryButton(
                        text = "Compartir código",
                        icon = share,
                        enabled = state.invitationCode != null,
                        onClick = { state.invitationCode?.let(onShare) }
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(top = SereniaSpacing.lg)
            ) {
                Text(
                    text = "Siempre lo encuentras en la pestaña Familia.",
                    fontFamily = Nunito,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SereniaPalette.Neutro700,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                PrimaryButton(text = "Entrar a Serenia", onClick = onEnter)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun InvitationStepContentPreview() {
    SereniaTheme {
        InvitationStepContent(
            state = InvitationStepUiState(
                invitationCode = InvitationCode(id = "1", code = "K7M4QX2P", expiresAtMillis = 1_778_250_600_000)
            ),
            onBack = {},
            onShare = {},
            onRetry = {},
            onEnter = {}
        )
    }
}
