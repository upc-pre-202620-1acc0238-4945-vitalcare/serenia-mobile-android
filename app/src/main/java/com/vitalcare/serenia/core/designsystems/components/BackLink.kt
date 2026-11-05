package com.vitalcare.serenia.core.designsystems.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.chevronLeft
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystems.theme.SereniaTouch

@Composable
fun BackLink(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "Atrás"
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .heightIn(min = SereniaTouch.senior)
            .clip(RoundedCornerShape(12.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(end = 8.dp)
    ) {
        Icon(
            imageVector = chevronLeft,
            contentDescription = null,
            tint = SereniaPalette.Neutro700,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            fontFamily = Nunito,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold,
            color = SereniaPalette.Neutro700
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BackLinkPreview() {
    SereniaTheme {
        BackLink(onClick = {})
    }
}
