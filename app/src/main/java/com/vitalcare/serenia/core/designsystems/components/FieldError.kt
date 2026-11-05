package com.vitalcare.serenia.core.designsystems.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.errorOutline
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun FieldError(
    message: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = errorOutline,
            contentDescription = null,
            tint = SereniaPalette.Error,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = message,
            fontFamily = Nunito,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold,
            color = SereniaPalette.Neutro800
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FieldErrorPreview() {
    SereniaTheme {
        FieldError(message = "Escribe tu nombre.")
    }
}
