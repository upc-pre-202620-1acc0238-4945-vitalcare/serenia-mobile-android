package com.vitalcare.serenia.features.socialcompanionship.presentation.photodetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystem.components.SereniaBackground
import com.vitalcare.serenia.core.designsystem.icon.chevronLeft
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.features.socialcompanionship.presentation.family.components.PhotoPlaceholder

@Composable
fun PhotoDetailScreen(
    senderName: String,
    sentAt: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    SereniaBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            TextButton(
                onClick = onBack,
                modifier = Modifier.heightIn(min = 56.dp),
                contentPadding = PaddingValues(start = 0.dp, end = 12.dp)
            ) {
                Icon(
                    imageVector = chevronLeft,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Atrás", style = MaterialTheme.typography.titleLarge)
            }

            Spacer(modifier = Modifier.height(8.dp))

            PhotoPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .semantics { contentDescription = "foto de $senderName" }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "$senderName · $sentAt",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotoDetailScreenPreview() {
    SereniaTheme {
        PhotoDetailScreen(
            senderName = "Lucía",
            sentAt = "hoy",
            onBack = {}
        )
    }
}
