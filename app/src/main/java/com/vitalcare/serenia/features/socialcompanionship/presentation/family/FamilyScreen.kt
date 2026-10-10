package com.vitalcare.serenia.features.socialcompanionship.presentation.family

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystem.components.SereniaBackground
import com.vitalcare.serenia.core.designsystem.components.SereniaSecondaryButton
import com.vitalcare.serenia.core.designsystem.icon.family
import com.vitalcare.serenia.core.designsystem.icon.mic
import com.vitalcare.serenia.core.designsystem.theme.SereniaTheme
import com.vitalcare.serenia.features.socialcompanionship.domain.FamilyPhoto
import com.vitalcare.serenia.features.socialcompanionship.presentation.family.components.FamilyPhotoCard

@Composable
fun FamilyScreen(
    modifier: Modifier = Modifier,
    viewModel: FamilyViewModel = hiltViewModel(),
    onPhotoClick: (FamilyPhoto) -> Unit,
    onRecordClick: () -> Unit,
    onCircleClick: () -> Unit
) {

    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value

    FamilyContent(
        uiState = uiState,
        onPhotoClick = onPhotoClick,
        onRecordClick = onRecordClick,
        onCircleClick = onCircleClick,
        modifier = modifier
    )
}

@Composable
fun FamilyContent(
    uiState: FamilyUiState,
    onPhotoClick: (FamilyPhoto) -> Unit,
    onRecordClick: () -> Unit,
    onCircleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SereniaBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Mi familia",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(28.dp))
                    .padding(22.dp)
            ) {
                Text(
                    text = "Fotos que te enviaron",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (uiState.photos.isEmpty()) {
                    Text(
                        text = "Aún no tienes fotografías. Cuando tu familia te comparta una, aparecerá aquí.",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        lineHeight = 28.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        uiState.photos.forEach { photo ->
                            FamilyPhotoCard(
                                photo = photo,
                                onClick = { onPhotoClick(photo) }
                            )
                        }
                    }
                }
            }

            SereniaSecondaryButton(
                text = "Grabar un audio para mi familia",
                onClick = onRecordClick,
                icon = mic,
                modifier = Modifier.fillMaxWidth()
            )

            SereniaSecondaryButton(
                text = "Mi círculo y mi código",
                onClick = onCircleClick,
                icon = family,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FamilyScreenPreview() {
    SereniaTheme {
        FamilyContent(
            uiState = FamilyUiState(
                photos = listOf(
                    FamilyPhoto(id = 1, senderName = "Lucía", sentAt = "hoy"),
                    FamilyPhoto(id = 2, senderName = "Martín", sentAt = "ayer")
                )
            ),
            onPhotoClick = {},
            onRecordClick = {},
            onCircleClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FamilyScreenEmptyPreview() {
    SereniaTheme {
        FamilyContent(
            uiState = FamilyUiState(),
            onPhotoClick = {},
            onRecordClick = {},
            onCircleClick = {}
        )
    }
}
