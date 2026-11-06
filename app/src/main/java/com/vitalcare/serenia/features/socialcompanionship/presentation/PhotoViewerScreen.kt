package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.chevronLeft
import com.vitalcare.serenia.core.designsystems.chevronRight
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.BackLink
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaTouch
import kotlinx.coroutines.launch

@Composable
fun PhotoViewerScreen(
    photoMessageId: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FamilyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LifecycleResumeEffect(Unit) {
        viewModel.refresh()
        onPauseOrDispose { }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SereniaPalette.Neutro900)
    ) {
        BackLink(
            onClick = onBack,
            text = "Fotos",
            color = SereniaPalette.Neutro50,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp)
        )

        val photos = state.photos
        val loadError = state.loadError
        when {
            loadError != null && photos == null -> AlertBanner(
                message = loadError,
                modifier = Modifier.padding(20.dp)
            )

            photos == null -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(color = SereniaPalette.Neutro50)
            }

            photos.isNotEmpty() -> PhotoPager(
                photos = photos,
                initialPage = photos.indexOfFirst { it.photo.id == photoMessageId }.coerceAtLeast(0),
                onPhotoShown = viewModel::markAsViewed
            )
        }
    }
}

@Composable
private fun PhotoPager(
    photos: List<PhotoItem>,
    initialPage: Int,
    onPhotoShown: (String) -> Unit
) {
    val pagerState = rememberPagerState(initialPage = initialPage) { photos.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.settledPage, photos.size) {
        photos.getOrNull(pagerState.settledPage)?.let { onPhotoShown(it.photo.id) }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { page ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                PhotoImage(
                    url = photos[page].photo.mediaUrl,
                    position = page,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        val current = photos.getOrNull(pagerState.currentPage) ?: return@Column
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 20.dp)
        ) {
            PagerArrow(
                icon = chevronLeft,
                description = "Foto anterior",
                enabled = pagerState.currentPage > 0,
                onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } }
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .semantics { liveRegion = LiveRegionMode.Polite }
            ) {
                Text(
                    text = "De ${current.senderName}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = SereniaPalette.Neutro50,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "${current.photo.dayAndTimeText()} · ${pagerState.positionText(photos.size)}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = SereniaPalette.Neutro200,
                    textAlign = TextAlign.Center
                )
            }
            PagerArrow(
                icon = chevronRight,
                description = "Foto siguiente",
                enabled = pagerState.currentPage < photos.lastIndex,
                onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } }
            )
        }
    }
}

@Composable
private fun PagerArrow(
    icon: ImageVector,
    description: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = SereniaPalette.Neutro700,
            contentColor = SereniaPalette.Neutro50,
            disabledContainerColor = SereniaPalette.Neutro800,
            disabledContentColor = SereniaPalette.Neutro500
        ),
        modifier = Modifier
            .size(SereniaTouch.senior)
            .background(SereniaPalette.Neutro900, CircleShape)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
            modifier = Modifier.size(32.dp)
        )
    }
}

private fun PagerState.positionText(total: Int): String {
    return "${currentPage + 1} de $total"
}
