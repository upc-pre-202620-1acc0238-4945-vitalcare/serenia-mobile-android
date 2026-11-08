package com.vitalcare.serenia.features.account.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.chevronRight
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.ProfileAvatar
import com.vitalcare.serenia.core.designsystems.components.SecondaryButton
import com.vitalcare.serenia.core.designsystems.lock
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaContrast
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun ProfileCard(
    fullName: String,
    photoUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 96.dp)
            .background(SereniaPalette.Durazno100, shape)
            .border(BorderStroke(SereniaContrast.stroke(2.dp), SereniaContrast.border(SereniaPalette.Durazno300)), shape)
            .clip(shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        ProfileAvatar(fullName = fullName, photoUrl = photoUrl, size = 64.dp)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = fullName,
                fontFamily = Nunito,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Neutro800
            )
            Text(
                text = "Editar mis datos y foto",
                fontFamily = Nunito,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SereniaPalette.Durazno800
            )
        }
        Icon(
            imageVector = chevronRight,
            contentDescription = null,
            tint = SereniaPalette.Durazno800,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
fun AccountOptionRow(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp)
            .shadow(
                elevation = 1.dp,
                shape = shape,
                ambientColor = SereniaPalette.Teal700.copy(alpha = 0.08f),
                spotColor = SereniaPalette.Teal700.copy(alpha = 0.08f)
            )
            .background(SereniaPalette.Neutro50, shape)
            .clip(shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(44.dp)
                .background(SereniaPalette.Neutro200, CircleShape)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = SereniaPalette.Neutro800,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = text,
            fontFamily = Nunito,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SereniaPalette.Neutro800,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = chevronRight,
            contentDescription = null,
            tint = SereniaPalette.Teal700,
            modifier = Modifier.size(26.dp)
        )
    }
}

@Composable
fun SignOutConfirmationCard(
    isSigningOut: Boolean,
    errorMessage: String?,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    LaunchedEffect(Unit) {
        bringIntoViewRequester.bringIntoView()
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .bringIntoViewRequester(bringIntoViewRequester)
            .shadow(
                elevation = 6.dp,
                shape = shape,
                ambientColor = SereniaPalette.Teal700.copy(alpha = 0.16f),
                spotColor = SereniaPalette.Teal700.copy(alpha = 0.16f)
            )
            .background(SereniaPalette.Durazno100, shape)
            .border(BorderStroke(SereniaContrast.stroke(2.dp), SereniaContrast.border(SereniaPalette.Durazno400)), shape)
            .padding(18.dp)
    ) {
        Text(
            text = "¿Quieres cerrar sesión?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = SereniaPalette.Neutro800,
            modifier = Modifier.semantics { heading() }
        )
        Text(
            text = "Tendrás que escribir tu correo y contraseña para entrar de nuevo.",
            fontFamily = Nunito,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = SereniaPalette.Neutro700
        )
        errorMessage?.let { message ->
            AlertBanner(message = message)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            PrimaryButton(
                text = "Sí, salir",
                onClick = onConfirm,
                isLoading = isSigningOut,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = "Cancelar",
                onClick = onCancel,
                enabled = !isSigningOut,
                color = SereniaPalette.Durazno800,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun AccountCardsPreview() {
    SereniaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            ProfileCard(fullName = "Rosa Mendoza", photoUrl = null, onClick = {})
            AccountOptionRow(icon = lock, text = "Cambiar mi contraseña", onClick = {})
            SignOutConfirmationCard(isSigningOut = false, errorMessage = null, onConfirm = {}, onCancel = {})
        }
    }
}
