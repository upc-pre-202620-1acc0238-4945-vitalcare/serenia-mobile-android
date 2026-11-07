package com.vitalcare.serenia.core.designsystems.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun ProfileAvatar(
    fullName: String,
    photoUrl: String?,
    size: Dp,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val initial = fullName.trim().firstOrNull()?.uppercase().orEmpty()

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(SereniaPalette.Durazno300, CircleShape)
    ) {
        Text(
            text = initial,
            fontFamily = Nunito,
            fontSize = (size.value * 0.44f).sp,
            fontWeight = FontWeight.ExtraBold,
            color = SereniaPalette.Durazno900
        )
        if (photoUrl != null) {
            AsyncImage(
                model = photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(SereniaPalette.Neutro900.copy(alpha = 0.45f))
            ) {
                CircularProgressIndicator(
                    color = SereniaPalette.Neutro50,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(size * 0.4f)
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun ProfileAvatarPreview() {
    SereniaTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            ProfileAvatar(fullName = "Rosa Mendoza", photoUrl = null, size = 64.dp)
            ProfileAvatar(fullName = "Rosa Mendoza", photoUrl = null, size = 88.dp, isLoading = true)
        }
    }
}
