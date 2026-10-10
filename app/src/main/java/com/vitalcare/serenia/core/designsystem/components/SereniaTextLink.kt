package com.vitalcare.serenia.core.designsystem.components

import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme

/** Underlined text action used for secondary navigation such as "Ya tengo cuenta". */
@Composable
fun SereniaTextLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = 56.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.ExtraBold,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF4E9)
@Composable
fun SereniaTextLinkPreview() {
    SereniaTheme {
        SereniaTextLink(text = "Ya tengo cuenta", onClick = {})
    }
}
