package com.vitalcare.serenia.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystem.icon.checkCircle
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme

/**
 * Short confirmation shown at the bottom of the screen after the user completes an action.
 */
@Composable
fun SereniaSnackbar(
    message: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp, shape = shape)
            .background(MaterialTheme.colorScheme.onBackground, shape)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = checkCircle,
            contentDescription = "Confirmación",
            tint = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}

@Preview
@Composable
fun SereniaSnackbarPreview() {
    SereniaTheme {
        SereniaSnackbar(message = "Recordatorio cancelado")
    }
}
