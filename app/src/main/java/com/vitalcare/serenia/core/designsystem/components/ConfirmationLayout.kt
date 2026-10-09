package com.vitalcare.serenia.core.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme

/**
 * Centered message used after the user completes an action: an illustration,
 * a title, a short message and the actions to continue.
 */
@Composable
fun ConfirmationLayout(
    title: String,
    message: String,
    illustration: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    titleColor: Color = MaterialTheme.colorScheme.primary,
    actions: @Composable ColumnScope.() -> Unit
) {
    SereniaBackground(modifier = modifier) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            // The min height keeps the content centered while still allowing scroll on small screens
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .heightIn(min = maxHeight)
                    .padding(horizontal = 32.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                illustration()

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.displaySmall,
                    color = titleColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = null),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    content = actions
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConfirmationLayoutPreview() {
    SereniaTheme {
        ConfirmationLayout(
            title = "Gracias, Rosa. Tu familia sabrá que estás bien.",
            message = "Tu familia ya puede ver cómo amaneciste.",
            illustration = { SereniaLogo(size = 110.dp) }
        ) {
            SereniaSecondaryButton(text = "Volver al inicio", onClick = {})
        }
    }
}
