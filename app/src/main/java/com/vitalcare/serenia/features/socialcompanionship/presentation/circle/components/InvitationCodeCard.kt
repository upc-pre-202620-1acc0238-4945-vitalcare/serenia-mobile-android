package com.vitalcare.serenia.features.socialcompanionship.presentation.circle.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme

@Composable
fun InvitationCodeCard(
    code: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(28.dp))
            .padding(horizontal = 22.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Mi código de invitación",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = code,
            style = MaterialTheme.typography.displaySmall,
            fontFamily = FontFamily.SansSerif,
            fontSize = 52.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 14.sp,
            color = MaterialTheme.colorScheme.onPrimary,
            // The digits are read one by one, not as a single number
            modifier = Modifier.semantics {
                contentDescription = "Código " + code.toList().joinToString(" ")
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Tú decides quién se vincula. Se usa una sola vez.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9)
@Composable
fun InvitationCodeCardPreview() {
    SereniaTheme {
        InvitationCodeCard(
            code = "4821",
            modifier = Modifier.padding(16.dp)
        )
    }
}
