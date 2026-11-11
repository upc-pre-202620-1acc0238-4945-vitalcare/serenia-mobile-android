package com.vitalcare.serenia.core.designsystems.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Above this font scale two items no longer fit side by side without splitting their words
private const val LARGE_TEXT_FONT_SCALE = 1.15f

@Composable
fun isLargeText(): Boolean = LocalDensity.current.fontScale > LARGE_TEXT_FONT_SCALE

/**
 * Places two items side by side, or one below the other when the user has chosen a large font.
 * Each slot receives the modifier it must apply to fill its share of the width.
 */
@Composable
fun AdaptivePair(
    first: @Composable (Modifier) -> Unit,
    second: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier,
    sideBySideModifier: Modifier = Modifier,
    spacing: Dp = 12.dp
) {
    if (isLargeText()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = modifier.fillMaxWidth()
        ) {
            first(Modifier.fillMaxWidth())
            second(Modifier.fillMaxWidth())
        }
    } else {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing),
            modifier = modifier.then(sideBySideModifier)
        ) {
            first(Modifier.weight(1f))
            second(Modifier.weight(1f))
        }
    }
}
