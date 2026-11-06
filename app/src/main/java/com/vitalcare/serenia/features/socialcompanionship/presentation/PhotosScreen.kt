package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.BackLink
import com.vitalcare.serenia.core.designsystems.components.SecondaryButton
import com.vitalcare.serenia.core.designsystems.image
import com.vitalcare.serenia.core.designsystems.refresh
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaSpacing
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.format.formatDayAndMonth
import com.vitalcare.serenia.features.socialcompanionship.domain.PhotoMessage
import java.util.Calendar

@Composable
fun PhotosScreen(
    onBack: () -> Unit,
    onPhotoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FamilyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

    PhotosContent(
        state = state,
        modifier = modifier,
        onBack = onBack,
        onRetry = viewModel::refresh,
        onPhotoClick = onPhotoClick
    )
}

@Composable
fun PhotosContent(
    state: FamilyUiState,
    onBack: () -> Unit,
    onRetry: () -> Unit,
    onPhotoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro100)
    ) {
        val screenHeight = maxHeight

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(start = SereniaSpacing.screen, top = 12.dp, end = SereniaSpacing.screen, bottom = 24.dp)
        ) {
            Column {
                BackLink(onClick = onBack, text = "Mi familia")
                Text(
                    text = "Fotos de tu familia",
                    style = MaterialTheme.typography.headlineLarge,
                    color = SereniaPalette.Teal700,
                    modifier = Modifier.semantics { heading() }
                )
            }

            val photos = state.photos
            val loadError = state.loadError
            when {
                loadError != null && photos == null -> {
                    AlertBanner(message = loadError)
                    SecondaryButton(text = "Intentar de nuevo", icon = refresh, onClick = onRetry)
                }

                photos == null -> Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp)
                ) {
                    CircularProgressIndicator(color = SereniaPalette.Teal700)
                }

                photos.isEmpty() -> EmptyPhotos(
                    membersText = state.membersText,
                    modifier = Modifier.heightIn(min = (screenHeight - EMPTY_STATE_OFFSET).coerceAtLeast(0.dp))
                )

                else -> {
                    var position = 0
                    photos.groupBy { weekGroup(it.photo) }.forEach { (group, items) ->
                        Text(
                            text = group,
                            style = MaterialTheme.typography.titleMedium,
                            color = SereniaPalette.Neutro600,
                            modifier = Modifier.semantics { heading() }
                        )
                        items.chunked(2).forEach { row ->
                            PhotoTileRow(
                                items = row,
                                firstPosition = position,
                                onPhotoClick = { item -> onPhotoClick(item.photo.id) },
                                height = 160.dp
                            )
                            position += row.size
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyPhotos(membersText: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(104.dp)
                .background(SereniaPalette.Verde200, CircleShape)
        ) {
            Icon(
                imageVector = image,
                contentDescription = null,
                tint = SereniaPalette.Verde900,
                modifier = Modifier.size(52.dp)
            )
        }
        Text(
            text = "Aún no hay fotos",
            style = MaterialTheme.typography.headlineMedium,
            color = SereniaPalette.Neutro800,
            textAlign = TextAlign.Center,
            modifier = Modifier.semantics { heading() }
        )
        Text(
            text = "Cuando $membersText te envíe una foto, la verás aquí.",
            style = MaterialTheme.typography.bodyMedium,
            color = SereniaPalette.Neutro700,
            textAlign = TextAlign.Center
        )
    }
}

private fun weekGroup(photo: PhotoMessage): String {
    val startOfThisWeek = startOfWeek(Calendar.getInstance())
    val startOfLastWeek = (startOfThisWeek.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, -7) }
    val photoWeek = startOfWeek(photo.sentAtCalendar())

    return when {
        !photoWeek.before(startOfThisWeek) -> "Esta semana"
        !photoWeek.before(startOfLastWeek) -> "La semana pasada"
        else -> "Semana del " + formatDayAndMonth(
            photoWeek.get(Calendar.MONTH) + 1,
            photoWeek.get(Calendar.DAY_OF_MONTH)
        )
    }
}

private fun startOfWeek(calendar: Calendar): Calendar {
    return Calendar.getInstance().apply {
        clear()
        set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        val daysSinceMonday = (get(Calendar.DAY_OF_WEEK) + 5) % 7
        add(Calendar.DAY_OF_MONTH, -daysSinceMonday)
    }
}

private val EMPTY_STATE_OFFSET = 200.dp

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun PhotosContentPreview() {
    val now = System.currentTimeMillis()
    SereniaTheme {
        PhotosContent(
            state = FamilyUiState(
                members = emptyList(),
                photoMessages = listOf(
                    PhotoMessage("1", "a", "", now, isViewed = false),
                    PhotoMessage("2", "b", "", now - 86_400_000L, isViewed = true),
                    PhotoMessage("3", "a", "", now - 9 * 86_400_000L, isViewed = true)
                )
            ),
            onBack = {},
            onRetry = {},
            onPhotoClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun EmptyPhotosContentPreview() {
    SereniaTheme {
        PhotosContent(
            state = FamilyUiState(members = emptyList(), photoMessages = emptyList()),
            onBack = {},
            onRetry = {},
            onPhotoClick = {}
        )
    }
}
