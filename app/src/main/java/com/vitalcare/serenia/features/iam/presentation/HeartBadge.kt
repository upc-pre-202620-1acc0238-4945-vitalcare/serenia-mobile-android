package com.vitalcare.serenia.features.iam.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystem.icon.favorite
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme

val HeartCoral = Color(0xFFF2765B)

/** Small round badge with a heart, used on the illustrations to say "someone cares". */
@Composable
fun HeartBadge(
    modifier: Modifier = Modifier,
    size: Dp = 28.dp,
    onCoral: Boolean = false
) {
    Box(
        modifier = modifier
            .size(size)
            .background(if (onCoral) HeartCoral else Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = favorite,
            contentDescription = null,
            tint = if (onCoral) Color.White else HeartCoral,
            modifier = Modifier.size(size * 0.55f)
        )
    }
}

@Preview
@Composable
fun HeartBadgePreview() {
    SereniaTheme {
        HeartBadge()
    }
}
