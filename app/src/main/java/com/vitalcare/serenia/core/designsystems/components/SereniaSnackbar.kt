package com.vitalcare.serenia.core.designsystems.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.check
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystems.theme.SereniaTouch

@Composable
fun SereniaSnackbar(
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: () -> Unit = {}
) {
    val shape = RoundedCornerShape(SereniaRadius.md)

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 12.dp, shape = shape)
            .background(SereniaPalette.Neutro800, shape)
            .semantics { liveRegion = LiveRegionMode.Polite }
            .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .background(SereniaPalette.Verde300, CircleShape)
        ) {
            Icon(
                imageVector = check,
                contentDescription = null,
                tint = SereniaPalette.Verde900,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = message,
            fontFamily = Nunito,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.Bold,
            color = SereniaPalette.Neutro50,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 6.dp)
        )
        if (actionLabel != null) {
            Text(
                text = actionLabel,
                fontFamily = Nunito,
                fontSize = 18.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Durazno300,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier
                    .heightIn(min = SereniaTouch.min)
                    .clip(RoundedCornerShape(SereniaRadius.sm))
                    .clickable(role = Role.Button, onClick = onAction)
                    .padding(horizontal = 12.dp, vertical = 13.dp)
            )
        }
    }
}

@Preview
@Composable
fun SereniaSnackbarPreview() {
    SereniaTheme {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SereniaSnackbar(message = "Listo, guardamos tus datos.")
            SereniaSnackbar(message = "Listo, está hecho.", actionLabel = "Deshacer")
        }
    }
}
