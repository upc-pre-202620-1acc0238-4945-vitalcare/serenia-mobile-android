package com.vitalcare.serenia.features.iam.presentation.welcome.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystem.icon.check
import com.vitalcare.serenia.core.designsystem.icon.favorite
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystem.theme.forestDark
import com.vitalcare.serenia.core.designsystem.theme.sunriseDeep

private val CheckGreen = Color(0xFF5BC98A)

/** Translucent pill that shows an example of what the family sees. */
@Composable
fun StatusChip(
    text: String,
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(24.dp)

    Row(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.14f), shape)
            .border(1.dp, Color.White.copy(alpha = 0.25f), shape)
            .padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(iconBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = Color.White
        )
    }
}

@Composable
fun RespondedChip(modifier: Modifier = Modifier) = StatusChip(
    text = "Respondió hoy · 9:12",
    icon = check,
    iconBackground = CheckGreen,
    iconTint = forestDark,
    modifier = modifier
)

@Composable
fun AllGoodChip(modifier: Modifier = Modifier) = StatusChip(
    text = "Todo en orden",
    icon = favorite,
    iconBackground = sunriseDeep,
    iconTint = forestDark,
    modifier = modifier
)

@Preview(showBackground = true, backgroundColor = 0xFF173A33)
@Composable
fun StatusChipsPreview() {
    SereniaTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            RespondedChip()
            Spacer(modifier = Modifier.width(8.dp))
            AllGoodChip()
        }
    }
}
