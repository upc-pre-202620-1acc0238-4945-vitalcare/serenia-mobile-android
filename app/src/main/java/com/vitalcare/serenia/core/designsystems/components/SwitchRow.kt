package com.vitalcare.serenia.core.designsystems.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.check
import com.vitalcare.serenia.core.designsystems.contrast
import com.vitalcare.serenia.core.designsystems.simplifiedMode
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaContrast
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun SwitchRow(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconBackground: Color = SereniaPalette.Teal100,
    iconTint: Color = SereniaPalette.Teal800
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp)
            .shadow(
                elevation = 1.dp,
                shape = shape,
                ambientColor = SereniaPalette.Teal700.copy(alpha = 0.08f),
                spotColor = SereniaPalette.Teal700.copy(alpha = 0.08f)
            )
            .clip(shape)
            .background(SereniaPalette.Neutro50)
            .then(
                if (SereniaContrast.enabled) {
                    Modifier.border(BorderStroke(2.dp, SereniaContrast.border(SereniaPalette.Neutro400)), shape)
                } else {
                    Modifier
                }
            )
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            )
            .semantics { stateDescription = if (checked) "Activado" else "Desactivado" }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .background(iconBackground, CircleShape)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = Nunito,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Neutro800
            )
            Text(
                text = description,
                fontFamily = Nunito,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = SereniaPalette.Neutro700
            )
        }
        SwitchTrack(checked = checked)
    }
}

@Composable
private fun SwitchTrack(checked: Boolean) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) SereniaPalette.Teal700 else SereniaPalette.Neutro400,
        label = "trackColor"
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 32.dp else 4.dp,
        label = "thumbOffset"
    )

    Box(
        modifier = Modifier
            .size(width = 64.dp, height = 36.dp)
            .background(trackColor, CircleShape)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset(x = thumbOffset, y = 4.dp)
                .size(28.dp)
                .shadow(elevation = 1.dp, shape = CircleShape)
                .background(SereniaPalette.Neutro50, CircleShape)
        ) {
            if (checked) {
                Icon(
                    imageVector = check,
                    contentDescription = null,
                    tint = SereniaPalette.Teal700,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun SwitchRowPreview() {
    SereniaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            SwitchRow(
                icon = simplifiedMode,
                title = "Modo simplificado",
                description = "Solo cómo estás y pedir ayuda",
                checked = true,
                onCheckedChange = {},
                iconBackground = SereniaPalette.Durazno200,
                iconTint = SereniaPalette.Durazno900
            )
            SwitchRow(
                icon = contrast,
                title = "Alto contraste",
                description = "Letras y bordes más marcados",
                checked = false,
                onCheckedChange = {}
            )
        }
    }
}
