package com.vitalcare.serenia.features.iam.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.chevronRight
import com.vitalcare.serenia.core.designsystems.components.BackLink
import com.vitalcare.serenia.core.designsystems.favoriteBorder
import com.vitalcare.serenia.core.designsystems.peopleOutline
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.features.iam.domain.UserRole

@Composable
fun RoleSelectionScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onRoleSelected: (UserRole) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
            .verticalScroll(rememberScrollState())
            .padding(start = SereniaSpacing.authScreen, top = 12.dp, end = SereniaSpacing.authScreen, bottom = 24.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            BackLink(onClick = onBack)
            Text(
                text = "¿Quién eres?",
                style = MaterialTheme.typography.headlineLarge,
                color = SereniaPalette.Teal700,
                modifier = Modifier.semantics { heading() }
            )
            Text(
                text = "Así preparamos Serenia a tu medida.",
                style = MaterialTheme.typography.bodyMedium,
                color = SereniaPalette.Neutro700
            )
        }

        RoleCard(
            title = "Soy adulto mayor",
            description = "Quiero contarle a mi familia cómo estoy.",
            icon = favoriteBorder,
            background = SereniaPalette.Durazno100,
            border = SereniaPalette.Durazno200,
            iconBackground = SereniaPalette.Durazno200,
            iconTint = SereniaPalette.Durazno900,
            onClick = { onRoleSelected(UserRole.OLDER_ADULT) }
        )

        RoleCard(
            title = "Soy familiar",
            description = "Quiero saber cómo está mi mamá, papá o abuelo.",
            icon = peopleOutline,
            background = SereniaPalette.Verde50,
            border = SereniaPalette.Verde200,
            iconBackground = SereniaPalette.Verde200,
            iconTint = SereniaPalette.Verde900,
            onClick = {}
        )
    }
}

@Composable
private fun RoleCard(
    title: String,
    description: String,
    icon: ImageVector,
    background: Color,
    border: Color,
    iconBackground: Color,
    iconTint: Color,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Surface(
        onClick = onClick,
        shape = shape,
        color = background,
        border = BorderStroke(2.dp, border),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 120.dp)
            .shadow(
                elevation = 1.dp,
                shape = shape,
                ambientColor = SereniaPalette.Teal700.copy(alpha = 0.08f),
                spotColor = SereniaPalette.Teal700.copy(alpha = 0.08f)
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp, top = 20.dp, end = 16.dp, bottom = 20.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .background(iconBackground, CircleShape)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(36.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = SereniaPalette.Neutro800
                )
                Text(
                    text = description,
                    fontFamily = Nunito,
                    fontSize = 18.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SereniaPalette.Neutro700
                )
            }
            Icon(
                imageVector = chevronRight,
                contentDescription = null,
                tint = SereniaPalette.Teal700,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun RoleSelectionScreenPreview() {
    SereniaTheme {
        RoleSelectionScreen(onBack = {}, onRoleSelected = {})
    }
}
