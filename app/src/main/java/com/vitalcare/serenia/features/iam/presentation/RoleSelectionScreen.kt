package com.vitalcare.serenia.features.iam.presentation.roleselection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystem.components.SereniaBackground
import com.vitalcare.serenia.core.designsystem.icon.chevronRight
import com.vitalcare.serenia.core.designsystem.icon.favorite
import com.vitalcare.serenia.core.designsystem.icon.groups
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystem.theme.forestDark
import com.vitalcare.serenia.features.iam.domain.UserRole

private val FamilyCircle = Color(0xFF6DB88C)
private val OlderAdultCircle = Color(0xFFF2D7B6)

@Composable
fun RoleSelectionScreen(
    modifier: Modifier = Modifier,
    onRoleSelected: (UserRole) -> Unit
) {
    SereniaBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            StepProgress(currentStep = 1, totalSteps = 3)

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "¿Quién eres?",
                style = MaterialTheme.typography.displaySmall,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                color = forestDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Así preparamos Serenia a tu medida.",
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            RoleCard(
                title = "Soy familiar",
                description = "Quiero saber cómo está mi mamá, papá o abuelo.",
                icon = groups,
                iconBackground = FamilyCircle,
                onClick = { onRoleSelected(UserRole.FAMILY) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RoleCard(
                title = "Soy adulto mayor",
                description = "Quiero contarle a mi familia cómo estoy.",
                icon = favorite,
                iconBackground = OlderAdultCircle,
                onClick = { onRoleSelected(UserRole.OLDER_ADULT) }
            )
        }
    }
}

/** Thin progress bar made of one segment per step of the sign up flow. */
@Composable
private fun StepProgress(
    currentStep: Int,
    totalSteps: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .semantics { contentDescription = "Paso $currentStep de $totalSteps" },
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        repeat(totalSteps) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .background(
                        color = if (index < currentStep) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outlineVariant
                        },
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun RoleCard(
    title: String,
    description: String,
    icon: ImageVector,
    iconBackground: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(28.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 112.dp)
            .shadow(elevation = 3.dp, shape = shape, ambientColor = MaterialTheme.colorScheme.outline)
            .background(Color.White, shape)
            .clickable(role = Role.Button, onClickLabel = title, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(iconBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = forestDark,
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = forestDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = chevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9, heightDp = 780)
@Composable
fun RoleSelectionScreenPreview() {
    SereniaTheme {
        RoleSelectionScreen(onRoleSelected = {})
    }
}
