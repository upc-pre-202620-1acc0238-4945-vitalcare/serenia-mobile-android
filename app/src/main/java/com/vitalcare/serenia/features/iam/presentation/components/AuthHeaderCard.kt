package com.vitalcare.serenia.features.iam.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystem.components.SereniaLogo
import com.vitalcare.serenia.core.designsystem.icon.person
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystem.theme.forestDark
import com.vitalcare.serenia.core.designsystem.theme.leafLight

private val PeachTitle = Color(0xFF5A2E1A)

/** Mint card with the app logo, shown at the top of the sign in screen. */
@Composable
fun SignInHeaderCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    HeaderCard(
        title = title,
        subtitle = subtitle,
        titleColor = forestDark,
        subtitleColor = MaterialTheme.colorScheme.onSurfaceVariant,
        brush = Brush.horizontalGradient(
            listOf(
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.55f),
                leafLight.copy(alpha = 0.3f)
            )
        ),
        modifier = modifier
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .background(Color.White.copy(alpha = 0.55f), CircleShape)
                    .padding(8.dp)
                    .background(forestDark, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color.White, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    SereniaLogo(size = 34.dp)
                }
            }
            HeartBadge(size = 30.dp, onCoral = true)
        }
    }
}

/** Peach card with two people, shown at the top of the sign up screen. */
@Composable
fun SignUpHeaderCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    HeaderCard(
        title = title,
        subtitle = subtitle,
        titleColor = PeachTitle,
        subtitleColor = PeachTitle,
        brush = Brush.horizontalGradient(
            listOf(
                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f)
            )
        ),
        modifier = modifier
    ) {
        Box(contentAlignment = Alignment.TopCenter) {
            Row {
                PersonBubble(color = forestDark, iconTint = leafLight)
                // The second person overlaps the first one
                PersonBubble(
                    color = HeartCoral,
                    iconTint = Color(0xFFFFE3D8),
                    modifier = Modifier.offset(x = (-14).dp)
                )
            }
            HeartBadge(size = 26.dp)
        }
    }
}

@Composable
private fun PersonBubble(color: Color, iconTint: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(68.dp)
            .background(Color.White, CircleShape)
            .padding(4.dp)
            .background(color, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = person,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(38.dp)
        )
    }
}

@Composable
private fun HeaderCard(
    title: String,
    subtitle: String,
    titleColor: Color,
    subtitleColor: Color,
    brush: Brush,
    modifier: Modifier = Modifier,
    illustration: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(brush, RoundedCornerShape(32.dp))
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 30.sp,
                lineHeight = 33.sp,
                color = titleColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.titleMedium,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = subtitleColor
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        illustration()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9)
@Composable
fun AuthHeaderCardsPreview() {
    SereniaTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            SignInHeaderCard(
                title = "Qué bueno verte de nuevo",
                subtitle = "Entra y mira cómo va tu día."
            )
            Spacer(modifier = Modifier.height(16.dp))
            SignUpHeaderCard(
                title = "Crea tu cuenta",
                subtitle = "Solo necesitamos tus datos básicos para vincularte."
            )
        }
    }
}
