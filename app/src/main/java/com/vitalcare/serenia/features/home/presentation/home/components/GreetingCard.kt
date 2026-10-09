package com.vitalcare.serenia.features.home.presentation.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystem.components.SereniaLogo
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystem.theme.forestDark
import com.vitalcare.serenia.core.designsystem.theme.sunriseDeep
import com.vitalcare.serenia.core.designsystem.theme.sunriseGlow
import com.vitalcare.serenia.core.designsystem.theme.sunriseLight

@Composable
fun GreetingCard(
    formattedDate: String,
    greeting: String,
    userName: String,
    modifier: Modifier = Modifier
) {
    val waveColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(28.dp))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(brush = Brush.verticalGradient(listOf(sunriseLight, sunriseDeep)))

            // Rising sun on the top right corner
            val sunCenter = Offset(size.width * 0.85f, size.height * 0.15f)
            drawCircle(color = sunriseGlow.copy(alpha = 0.35f), radius = size.width * 0.38f, center = sunCenter)
            drawCircle(color = sunriseGlow.copy(alpha = 0.45f), radius = size.width * 0.26f, center = sunCenter)
            drawCircle(color = sunriseGlow.copy(alpha = 0.6f), radius = size.width * 0.15f, center = sunCenter)

            // Hills at the bottom of the card
            val backHill = Path().apply {
                moveTo(0f, size.height * 0.72f)
                quadraticTo(size.width * 0.3f, size.height * 0.58f, size.width * 0.6f, size.height * 0.68f)
                quadraticTo(size.width * 0.85f, size.height * 0.76f, size.width, size.height * 0.66f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            val frontHill = Path().apply {
                moveTo(0f, size.height * 0.78f)
                quadraticTo(size.width * 0.45f, size.height * 0.68f, size.width, size.height * 0.76f)
                lineTo(size.width, size.height)
                lineTo(0f, size.height)
                close()
            }
            drawPath(path = backHill, color = waveColor.copy(alpha = 0.75f))
            drawPath(
                path = frontHill,
                brush = Brush.horizontalGradient(listOf(waveColor, forestDark.copy(alpha = 0.9f)))
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SereniaLogo(size = 28.dp)
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
            Text(
                text = "$greeting,\n$userName",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

        Text(
            text = "¿Cómo amaneciste hoy?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 22.dp, vertical = 16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingCardPreview() {
    SereniaTheme {
        GreetingCard(
            formattedDate = "Lun 5 oct",
            greeting = "Buenos días",
            userName = "Rosa",
            modifier = Modifier.padding(16.dp)
        )
    }
}
