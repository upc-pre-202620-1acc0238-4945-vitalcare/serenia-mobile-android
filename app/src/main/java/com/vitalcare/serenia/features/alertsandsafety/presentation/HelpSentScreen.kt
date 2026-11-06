package com.vitalcare.serenia.features.alertsandsafety.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystems.check
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.errorOutline
import com.vitalcare.serenia.core.designsystems.peopleOutline
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun HelpSentScreen(
    outcome: HelpOutcome,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val content = when (outcome) {
        HelpOutcome.NOTIFIED -> HelpSentContent(
            icon = check,
            iconBackground = SereniaPalette.Verde100,
            iconTint = SereniaPalette.Verde800,
            title = "Ya avisamos a tu familia",
            body = "Tu aviso les llegó a todos al mismo tiempo. Pronto se comunicarán contigo.",
            action = "Volver al inicio"
        )
        HelpOutcome.NO_FAMILY -> HelpSentContent(
            icon = peopleOutline,
            iconBackground = SereniaPalette.Durazno200,
            iconTint = SereniaPalette.Durazno900,
            title = "Aún no hay familiares en tu círculo",
            body = "Guardamos tu aviso, pero todavía no tenemos a quién enviarlo. " +
                "Si es urgente, llama a alguien de confianza o a emergencias.",
            action = "Volver al inicio"
        )
        HelpOutcome.FAILED -> HelpSentContent(
            icon = errorOutline,
            iconBackground = SereniaPalette.ErrorBg,
            iconTint = SereniaPalette.Error,
            title = "No pudimos enviar tu aviso",
            body = "Revisa tu internet y vuelve a mantener presionado «Pedir ayuda». " +
                "Si es urgente, llama a alguien de confianza o a emergencias.",
            action = "Intentar de nuevo"
        )
    }

    HelpSentLayout(content = content, onAction = onBackToHome, modifier = modifier)
}

data class HelpSentContent(
    val icon: ImageVector,
    val iconBackground: Color,
    val iconTint: Color,
    val title: String,
    val body: String,
    val action: String
)

@Composable
fun HelpSentLayout(
    content: HelpSentContent,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
    ) {
        val screenHeight = maxHeight

        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .heightIn(min = screenHeight)
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 24.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(112.dp)
                    .background(content.iconBackground, CircleShape)
            ) {
                Icon(
                    imageVector = content.icon,
                    contentDescription = null,
                    tint = content.iconTint,
                    modifier = Modifier.size(64.dp)
                )
            }
            Text(
                text = content.title,
                style = MaterialTheme.typography.headlineLarge,
                color = SereniaPalette.Neutro800,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = content.body,
                style = MaterialTheme.typography.bodyLarge,
                color = SereniaPalette.Neutro700,
                textAlign = TextAlign.Center
            )
            PrimaryButton(
                text = content.action,
                onClick = onAction,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun HelpSentScreenPreview() {
    SereniaTheme {
        HelpSentScreen(outcome = HelpOutcome.NOTIFIED, onBackToHome = {})
    }
}
