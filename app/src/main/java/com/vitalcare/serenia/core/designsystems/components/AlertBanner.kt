package com.vitalcare.serenia.core.designsystems.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.errorOutline
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun AlertBanner(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
        modifier = modifier
            .fillMaxWidth()
            .background(SereniaPalette.ErrorBg, RoundedCornerShape(SereniaRadius.md))
            .padding(16.dp)
            .semantics { liveRegion = LiveRegionMode.Assertive }
    ) {
        Icon(
            imageVector = errorOutline,
            contentDescription = null,
            tint = SereniaPalette.Error,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = message,
            fontFamily = Nunito,
            fontSize = 18.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.Bold,
            color = SereniaPalette.Neutro800
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AlertBannerPreview() {
    SereniaTheme {
        AlertBanner(message = "El correo o la contraseña no son correctos. Revisa e intenta de nuevo.")
    }
}
