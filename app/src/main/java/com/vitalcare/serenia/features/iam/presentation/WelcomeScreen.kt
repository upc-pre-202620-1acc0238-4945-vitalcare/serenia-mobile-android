package com.vitalcare.serenia.features.iam.presentation.welcome

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystem.components.SereniaBackground
import com.vitalcare.serenia.core.designsystem.components.SereniaLogo
import com.vitalcare.serenia.core.designsystem.components.SereniaPrimaryButton
import com.vitalcare.serenia.core.designsystem.components.SereniaTextLink
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystem.theme.dotCoral
import com.vitalcare.serenia.core.designsystem.theme.forestDark
import com.vitalcare.serenia.features.iam.presentation.welcome.components.AllGoodChip
import com.vitalcare.serenia.features.iam.presentation.welcome.components.ConnectionIllustration
import com.vitalcare.serenia.features.iam.presentation.welcome.components.RespondedChip

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit,
    onHaveAccountClick: () -> Unit
) {
    SereniaBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WelcomeHero()

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = "Te damos la bienvenida",
                    style = MaterialTheme.typography.headlineLarge,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.ExtraBold,
                    color = forestDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Un check-in diario de un toque: ellos saben que estás bien y tú sabes que les importas.",
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    lineHeight = 26.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SereniaPrimaryButton(
                text = "Empezar",
                onClick = onStartClick,
                fontFamily = FontFamily.Default,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            SereniaTextLink(text = "Ya tengo cuenta", onClick = onHaveAccountClick)
        }
    }
}

@Composable
private fun WelcomeHero(modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(36.dp)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                Brush.linearGradient(
                    colors = listOf(forestDark, MaterialTheme.colorScheme.primary)
                )
            )
    ) {
        // Soft rings behind the illustration
        Canvas(modifier = Modifier.matchParentSize()) {
            val center = Offset(size.width * 0.5f, size.height * 0.55f)
            drawCircle(Color.White.copy(alpha = 0.06f), radius = size.width * 0.34f, center = center, style = Stroke(width = 1.5.dp.toPx()))
            drawCircle(Color.White.copy(alpha = 0.05f), radius = size.width * 0.5f, center = center, style = Stroke(width = 1.5.dp.toPx()))
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color.White, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    SereniaLogo(size = 34.dp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Serenia",
                    style = MaterialTheme.typography.titleLarge,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Color.White)) { append("Bienestar hoy,\n") }
                    withStyle(SpanStyle(color = dotCoral)) { append("siempre contigo.") }
                },
                style = MaterialTheme.typography.headlineLarge,
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 40.sp,
                lineHeight = 44.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            ConnectionIllustration()

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RespondedChip()
                AllGoodChip(modifier = Modifier.align(Alignment.End))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9, heightDp = 780)
@Composable
fun WelcomeScreenPreview() {
    SereniaTheme {
        WelcomeScreen(onStartClick = {}, onHaveAccountClick = {})
    }
}
