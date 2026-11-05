package com.vitalcare.serenia.features.iam.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.favorite
import com.vitalcare.serenia.core.designsystems.sentimentSatisfied
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

private const val ILLUSTRATION_DESCRIPTION =
    "Rosa le cuenta a su hija Lucía que amaneció muy bien, y Lucía le responde con cariño."

@Composable
fun WelcomeIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(330.dp)
            .clearAndSetSemantics { contentDescription = ILLUSTRATION_DESCRIPTION }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val scale = size.width / 342.dp.toPx()
            val path = Path().apply {
                moveTo(80.dp.toPx() * scale, 180.dp.toPx())
                cubicTo(
                    110.dp.toPx() * scale, 70.dp.toPx(),
                    210.dp.toPx() * scale, 40.dp.toPx(),
                    282.dp.toPx() * scale, 64.dp.toPx()
                )
            }
            drawPath(
                path = path,
                color = SereniaPalette.Durazno300,
                style = Stroke(
                    width = 4.dp.toPx(),
                    cap = StrokeCap.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(2.dp.toPx(), 12.dp.toPx()))
                )
            )
        }

        RingedCircle(
            size = 44.dp,
            ring = 6.dp,
            color = SereniaPalette.Durazno300,
            ringColor = SereniaPalette.Durazno300.copy(alpha = 0.25f),
            modifier = Modifier.offset(x = 144.dp, y = 70.dp)
        ) {
            Icon(
                imageVector = favorite,
                contentDescription = null,
                tint = SereniaPalette.Error,
                modifier = Modifier.size(24.dp)
            )
        }

        RingedCircle(
            size = 132.dp,
            ring = 10.dp,
            color = SereniaPalette.Durazno200,
            ringColor = SereniaPalette.Durazno300.copy(alpha = 0.18f),
            modifier = Modifier.offset(x = 2.dp, y = 106.dp)
        ) {
            FaceIcon(icon = sentimentSatisfied, tint = SereniaPalette.Durazno900, size = 76.dp)
        }

        RingedCircle(
            size = 104.dp,
            ring = 10.dp,
            color = SereniaPalette.Verde300,
            ringColor = SereniaPalette.Verde300.copy(alpha = 0.18f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-2).dp, y = (-2).dp)
        ) {
            FaceIcon(icon = sentimentSatisfied, tint = SereniaPalette.Verde900, size = 60.dp)
        }

        MessageBubble(
            sender = "Lucía, su hija",
            message = "¡Qué bueno, mamá!",
            senderColor = SereniaPalette.Durazno800,
            background = SereniaPalette.Durazno100,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 6.dp, bottomEnd = 20.dp, bottomStart = 20.dp),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(y = 132.dp)
        )

        MessageBubble(
            sender = "Rosa · 8:00 a. m.",
            message = "Amanecí muy bien",
            senderColor = SereniaPalette.Teal800,
            background = SereniaPalette.Neutro50,
            shape = RoundedCornerShape(topStart = 6.dp, topEnd = 20.dp, bottomEnd = 20.dp, bottomStart = 20.dp),
            modifier = Modifier.offset(y = 266.dp)
        )
    }
}

@Composable
private fun RingedCircle(
    size: Dp,
    ring: Dp,
    color: Color,
    ringColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size + ring * 2)
            .background(ringColor, CircleShape)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .background(color, CircleShape),
            content = content
        )
    }
}

@Composable
private fun FaceIcon(icon: ImageVector, tint: Color, size: Dp) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = tint,
        modifier = Modifier.size(size)
    )
}

@Composable
private fun MessageBubble(
    sender: String,
    message: String,
    senderColor: Color,
    background: Color,
    shape: Shape,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = shape,
                ambientColor = SereniaPalette.Teal700.copy(alpha = 0.22f),
                spotColor = SereniaPalette.Teal700.copy(alpha = 0.22f)
            )
            .background(background, shape)
            .padding(start = 16.dp, top = 10.dp, end = 16.dp, bottom = 12.dp)
    ) {
        Text(
            text = sender,
            fontFamily = Nunito,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Bold,
            color = senderColor
        )
        Text(
            text = message,
            fontFamily = Nunito,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SereniaPalette.Neutro800
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1F6F66)
@Composable
fun WelcomeIllustrationPreview() {
    SereniaTheme {
        WelcomeIllustration(modifier = Modifier.padding(24.dp))
    }
}
