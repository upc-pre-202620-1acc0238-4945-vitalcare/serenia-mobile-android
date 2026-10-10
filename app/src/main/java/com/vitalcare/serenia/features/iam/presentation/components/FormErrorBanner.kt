package com.vitalcare.serenia.features.iam.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme

/** Message that tells the user, in plain words, what to fix in the form. */
@Composable
fun FormErrorBanner(
    message: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = message,
        style = MaterialTheme.typography.titleMedium,
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onErrorContainer,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.errorContainer, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
            // Screen readers announce the message as soon as it appears
            .semantics { liveRegion = LiveRegionMode.Polite }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9)
@Composable
fun FormErrorBannerPreview() {
    SereniaTheme {
        FormErrorBanner(
            message = "Revisa los campos marcados: escribe tu nombre, un correo válido y una contraseña de al menos 8 caracteres.",
            modifier = Modifier.padding(16.dp)
        )
    }
}
