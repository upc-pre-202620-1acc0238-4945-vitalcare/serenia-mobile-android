package com.vitalcare.serenia.features.iam.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.R
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit,
    onHaveAccountClick: () -> Unit
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Teal700)
    ) {
        val screenHeight = maxHeight

        Box(
            modifier = Modifier
                .offset(x = (-150).dp, y = 120.dp)
                .size(360.dp)
                .background(SereniaPalette.Teal600, CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 140.dp, y = (-150).dp)
                .size(300.dp)
                .background(SereniaPalette.Teal800, CircleShape)
        )

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .heightIn(min = screenHeight)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(start = SereniaSpacing.authScreen, top = 20.dp, end = SereniaSpacing.authScreen, bottom = 28.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                WelcomeHeader()

                Box {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (-16).dp, y = 32.dp)
                            .size(18.dp)
                            .alpha(0.8f)
                            .background(SereniaPalette.Durazno300, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .offset(x = 12.dp, y = 8.dp)
                            .size(12.dp)
                            .alpha(0.8f)
                            .background(SereniaPalette.Verde300, CircleShape)
                    )
                    WelcomeIllustration()
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(
                        text = "Tu familia, más cerca",
                        style = MaterialTheme.typography.headlineLarge,
                        letterSpacing = (-0.005).em,
                        color = SereniaPalette.Neutro50,
                        modifier = Modifier.semantics { heading() }
                    )
                    Text(
                        text = "Respondes con un toque y tu familia sabe cómo estás, sin tener que llamar.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SereniaPalette.Teal100
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = SereniaSpacing.lg)
            ) {
                PrimaryButton(
                    text = "Crear mi cuenta",
                    onClick = onStartClick,
                    containerColor = SereniaPalette.Durazno300,
                    contentColor = SereniaPalette.Teal900
                )
                PrimaryButton(
                    text = "Ya tengo cuenta",
                    onClick = onHaveAccountClick,
                    containerColor = SereniaPalette.Neutro50,
                    contentColor = SereniaPalette.Teal800,
                    elevated = false
                )
            }
        }
    }
}

@Composable
private fun WelcomeHeader() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .background(SereniaPalette.Neutro50, CircleShape)
        ) {
            Image(
                painter = painterResource(R.drawable.serenia_isotipo),
                contentDescription = null,
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
            )
        }
        Text(
            text = "Serenia",
            fontFamily = Nunito,
            fontSize = 28.sp,
            lineHeight = 34.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = (-0.01).em,
            color = SereniaPalette.Neutro50
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    SereniaTheme {
        WelcomeScreen(onStartClick = {}, onHaveAccountClick = {})
    }
}
