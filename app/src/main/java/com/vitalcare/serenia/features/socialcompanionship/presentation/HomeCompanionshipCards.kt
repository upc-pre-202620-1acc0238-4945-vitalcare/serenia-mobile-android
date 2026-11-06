package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vitalcare.serenia.core.designsystems.image
import com.vitalcare.serenia.core.designsystems.mic
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.format.joinNames
import com.vitalcare.serenia.features.socialcompanionship.domain.PhotoMessage

@Composable
fun HomeCompanionshipCards(
    onTellSomething: () -> Unit,
    onSeePhotos: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FamilyViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RefreshWhileResumed(onRefresh = viewModel::refresh)

    HomeCompanionshipCardsContent(
        state = state,
        onTellSomething = onTellSomething,
        onSeePhotos = onSeePhotos,
        modifier = modifier
    )
}

@Composable
fun HomeCompanionshipCardsContent(
    state: FamilyUiState,
    onTellSomething: () -> Unit,
    onSeePhotos: () -> Unit,
    modifier: Modifier = Modifier
) {
    val newPhotos = state.newPhotos
    val photosTitle = if (newPhotos.isEmpty()) "Fotos" else "Fotos nuevas"
    val photosSubtitle = if (newPhotos.isEmpty()) {
        "De tu familia"
    } else {
        "De ${joinNames(newPhotos.map { it.senderName }.distinct())}"
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        HomeShortcutCard(
            title = "Cuéntame algo",
            subtitle = "Graba un audio para tu familia",
            background = SereniaPalette.Durazno100,
            border = SereniaPalette.Durazno300,
            onClick = onTellSomething,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .background(SereniaPalette.Durazno700, CircleShape)
            ) {
                Icon(
                    imageVector = mic,
                    contentDescription = null,
                    tint = SereniaPalette.Neutro50,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        HomeShortcutCard(
            title = photosTitle,
            subtitle = photosSubtitle,
            background = SereniaPalette.Neutro50,
            border = SereniaPalette.Neutro200,
            onClick = onSeePhotos,
            accessibilityExtra = if (newPhotos.isEmpty()) "" else ". ${newPhotos.size} sin ver",
            modifier = Modifier.weight(1f)
        ) {
            Box {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(56.dp)
                        .background(SereniaPalette.Teal100, CircleShape)
                ) {
                    Icon(
                        imageVector = image,
                        contentDescription = null,
                        tint = SereniaPalette.Teal800,
                        modifier = Modifier.size(30.dp)
                    )
                }
                if (newPhotos.isNotEmpty()) {
                    Text(
                        text = newPhotos.size.toString(),
                        fontFamily = Nunito,
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SereniaPalette.Neutro50,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 14.dp, y = (-8).dp)
                            .heightIn(min = 28.dp)
                            .background(SereniaPalette.Teal700, CircleShape)
                            .padding(horizontal = 9.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeShortcutCard(
    title: String,
    subtitle: String,
    background: Color,
    border: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    accessibilityExtra: String = "",
    icon: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Column(
        modifier = modifier
            .fillMaxHeight()
            .heightIn(min = 184.dp)
            .clip(shape)
            .background(background, shape)
            .border(BorderStroke(2.dp, border), shape)
            .clickable(onClick = onClick)
            .clearAndSetSemantics {
                contentDescription = "$title. $subtitle$accessibilityExtra"
                role = Role.Button
                onClick { onClick(); true }
            }
            .padding(16.dp)
    ) {
        icon()
        Spacer(modifier = Modifier.weight(1f).heightIn(min = 16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = SereniaPalette.Neutro800
        )
        Text(
            text = subtitle,
            fontFamily = Nunito,
            fontSize = 18.sp,
            lineHeight = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = SereniaPalette.Neutro700
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun HomeCompanionshipCardsContentPreview() {
    val now = System.currentTimeMillis()
    SereniaTheme {
        HomeCompanionshipCardsContent(
            state = FamilyUiState(
                members = emptyList(),
                photoMessages = listOf(
                    PhotoMessage("1", "a", "", now, isViewed = false),
                    PhotoMessage("2", "b", "", now, isViewed = false)
                )
            ),
            onTellSomething = {},
            onSeePhotos = {},
            modifier = Modifier.padding(20.dp)
        )
    }
}
