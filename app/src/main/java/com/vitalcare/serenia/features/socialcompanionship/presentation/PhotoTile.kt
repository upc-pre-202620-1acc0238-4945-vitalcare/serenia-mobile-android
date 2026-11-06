package com.vitalcare.serenia.features.socialcompanionship.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.vitalcare.serenia.core.designsystems.image
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.format.formatPastDay
import com.vitalcare.serenia.core.format.formatTime
import com.vitalcare.serenia.features.socialcompanionship.domain.PhotoMessage
import java.util.Calendar

@Composable
fun PhotoTile(
    item: PhotoItem,
    position: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 150.dp
) {
    val caption = "${item.senderName} · ${item.photo.dayText()}"
    val isNew = !item.photo.isViewed

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .clip(RoundedCornerShape(SereniaRadius.md))
            .clickable(onClick = onClick)
            .clearAndSetSemantics {
                contentDescription = "Foto de ${item.senderName}, ${item.photo.dayText()}" +
                    if (isNew) ". Nueva" else ""
                role = Role.Button
                onClick { onClick(); true }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height)
                .clip(RoundedCornerShape(SereniaRadius.md))
        ) {
            PhotoImage(
                url = item.photo.mediaUrl,
                position = position,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            if (isNew) {
                NewBadge(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp)
                )
            }
        }
        Text(
            text = caption,
            fontFamily = Nunito,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.Bold,
            color = SereniaPalette.Neutro800
        )
    }
}

@Composable
fun PhotoTileRow(
    items: List<PhotoItem>,
    firstPosition: Int,
    onPhotoClick: (PhotoItem) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 150.dp
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items.forEachIndexed { index, item ->
            PhotoTile(
                item = item,
                position = firstPosition + index,
                onClick = { onPhotoClick(item) },
                height = height,
                modifier = Modifier.weight(1f)
            )
        }
        if (items.size == 1) {
            Box(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun PhotoImage(
    url: String,
    position: Int,
    contentScale: ContentScale,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(placeholderColor(position))
    ) {
        Icon(
            imageVector = image,
            contentDescription = null,
            tint = SereniaPalette.Neutro600,
            modifier = Modifier.size(40.dp)
        )
        AsyncImage(
            model = url,
            contentDescription = null,
            contentScale = contentScale,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun NewBadge(modifier: Modifier = Modifier) {
    Text(
        text = "Nueva",
        fontFamily = Nunito,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        fontWeight = FontWeight.ExtraBold,
        color = SereniaPalette.Neutro50,
        modifier = modifier
            .background(SereniaPalette.Durazno800, CircleShape)
            .padding(horizontal = 10.dp, vertical = 2.dp)
    )
}

fun PhotoMessage.sentAtCalendar(): Calendar {
    return Calendar.getInstance().apply { timeInMillis = sentAtMillis }
}

fun PhotoMessage.dayText(): String {
    return formatPastDay(sentAtCalendar())
}

fun PhotoMessage.dayAndTimeText(): String {
    val calendar = sentAtCalendar()
    val day = formatPastDay(calendar).replaceFirstChar { it.uppercase() }
    return "$day, ${formatTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))}"
}

private fun placeholderColor(position: Int): Color {
    return PLACEHOLDER_COLORS[position % PLACEHOLDER_COLORS.size]
}

private val PLACEHOLDER_COLORS = listOf(
    SereniaPalette.Verde100,
    SereniaPalette.Durazno100,
    SereniaPalette.Teal50,
    SereniaPalette.Verde50
)

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun PhotoTileRowPreview() {
    val now = System.currentTimeMillis()
    SereniaTheme {
        PhotoTileRow(
            items = listOf(
                PhotoItem(PhotoMessage("1", "a", "", now, isViewed = false), "Lucía"),
                PhotoItem(PhotoMessage("2", "b", "", now - 86_400_000L, isViewed = true), "Martín")
            ),
            firstPosition = 0,
            onPhotoClick = {},
            modifier = Modifier.padding(20.dp)
        )
    }
}
