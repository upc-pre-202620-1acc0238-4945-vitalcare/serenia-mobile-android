package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.chevronRight
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.SereniaSnackbar
import com.vitalcare.serenia.core.designsystems.components.TextLinkButton
import com.vitalcare.serenia.core.designsystems.mic
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.features.carecircle.presentation.CircleSummaryCard
import com.vitalcare.serenia.features.carecircle.presentation.FamilyMember
import com.vitalcare.serenia.features.socialcompanionship.domain.PhotoMessage
import kotlinx.coroutines.delay

@Composable
fun FamilyScreen(
    message: String?,
    onMessageShown: () -> Unit,
    onRecord: () -> Unit,
    onSeePhotos: () -> Unit,
    onPhotoClick: (String) -> Unit,
    onCircle: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FamilyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

    LaunchedEffect(message) {
        if (message != null) {
            viewModel.showMessage(message)
            onMessageShown()
        }
    }

    LaunchedEffect(state.message) {
        if (state.message != null) {
            delay(MESSAGE_DURATION_MILLIS)
            viewModel.onMessageShown()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        FamilyContent(
            state = state,
            onRecord = onRecord,
            onSeePhotos = onSeePhotos,
            onPhotoClick = onPhotoClick,
            onCircle = onCircle
        )

        state.message?.let { text ->
            SereniaSnackbar(
                message = text,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(SereniaSpacing.md)
            )
        }
    }
}

@Composable
fun FamilyContent(
    state: FamilyUiState,
    onRecord: () -> Unit,
    onSeePhotos: () -> Unit,
    onPhotoClick: (String) -> Unit,
    onCircle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
            .verticalScroll(rememberScrollState())
            .padding(start = SereniaSpacing.screen, top = 28.dp, end = SereniaSpacing.screen, bottom = 24.dp)
    ) {
        Text(
            text = "Mi familia",
            style = MaterialTheme.typography.headlineLarge,
            color = SereniaPalette.Teal700,
            modifier = Modifier.semantics { heading() }
        )

        TellSomethingCard(onClick = onRecord)

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                SectionTitle(text = "Fotos de tu familia")
                if (!state.photos.isNullOrEmpty()) {
                    TextLinkButton(
                        text = "Ver todas",
                        onClick = onSeePhotos,
                        color = SereniaPalette.Teal700,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            val photos = state.photos
            val loadError = state.loadError
            when {
                loadError != null && photos == null -> AlertBanner(message = loadError)

                photos == null -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 150.dp)
                ) {
                    CircularProgressIndicator(color = SereniaPalette.Teal700)
                }

                photos.isEmpty() -> Text(
                    text = "Aún no hay fotos. Cuando ${state.membersText} te envíe una, la verás aquí.",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SereniaPalette.Neutro700
                )

                else -> PhotoTileRow(
                    items = photos.take(LATEST_PHOTOS),
                    firstPosition = 0,
                    onPhotoClick = { item -> onPhotoClick(item.photo.id) }
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SectionTitle(text = "Mi círculo")
            CircleSummaryCard(members = state.members.orEmpty(), onClick = onCircle)
        }
    }
}

@Composable
private fun TellSomethingCard(onClick: () -> Unit) {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 104.dp)
            .clip(shape)
            .background(SereniaPalette.Durazno100, shape)
            .border(BorderStroke(2.dp, SereniaPalette.Durazno300), shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(start = 18.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .background(SereniaPalette.Durazno700, CircleShape)
        ) {
            Icon(
                imageVector = mic,
                contentDescription = null,
                tint = SereniaPalette.Neutro50,
                modifier = Modifier.size(34.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Cuéntame algo",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Neutro800
            )
            Text(
                text = "Graba un audio para tu familia",
                fontFamily = Nunito,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = SereniaPalette.Neutro700
            )
        }
        Icon(
            imageVector = chevronRight,
            contentDescription = null,
            tint = SereniaPalette.Durazno800,
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        color = SereniaPalette.Neutro800,
        modifier = Modifier.semantics { heading() }
    )
}

private const val LATEST_PHOTOS = 2
private const val MESSAGE_DURATION_MILLIS = 4_000L

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun FamilyContentPreview() {
    val now = System.currentTimeMillis()
    SereniaTheme {
        FamilyContent(
            state = FamilyUiState(
                members = listOf(
                    FamilyMember("1", "a", "Lucía Mendoza", "hija"),
                    FamilyMember("2", "b", "Martín Ríos", "nieto")
                ),
                photoMessages = listOf(
                    PhotoMessage("1", "a", "", now, isViewed = false),
                    PhotoMessage("2", "b", "", now - 86_400_000L, isViewed = true)
                )
            ),
            onRecord = {},
            onSeePhotos = {},
            onPhotoClick = {},
            onCircle = {}
        )
    }
}
