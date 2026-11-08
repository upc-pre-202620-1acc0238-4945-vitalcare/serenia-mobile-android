package com.vitalcare.serenia.features.carecircle.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.components.AlertBanner
import com.vitalcare.serenia.core.designsystems.components.PrimaryButton
import com.vitalcare.serenia.core.designsystems.components.SecondaryButton
import com.vitalcare.serenia.core.designsystems.components.TextLinkButton
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaContrast
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme

@Composable
fun FamilyMemberCard(
    member: FamilyMember,
    tone: MemberTone,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 84.dp)
            .shadow(
                elevation = 1.dp,
                shape = shape,
                ambientColor = SereniaPalette.Teal700.copy(alpha = 0.08f),
                spotColor = SereniaPalette.Teal700.copy(alpha = 0.08f)
            )
            .background(SereniaPalette.Neutro50, shape)
            .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
    ) {
        MemberAvatar(initials = member.initials, tone = tone, size = 56.dp)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = member.firstName,
                fontFamily = Nunito,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Neutro800
            )
            Text(
                text = member.relationship,
                fontFamily = Nunito,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium,
                color = SereniaPalette.Neutro700
            )
        }
        TextLinkButton(text = "Quitar", onClick = onRemove)
    }
}

@Composable
fun RemoveMemberCard(
    member: FamilyMember,
    tone: MemberTone,
    isRemoving: Boolean,
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
        verticalArrangement = Arrangement.spacedBy(14.dp),
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            MemberAvatar(initials = member.initials, tone = tone, size = 56.dp)
            Text(
                text = "¿Quitar a ${member.firstName} de tu círculo?",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Neutro800,
                modifier = Modifier
                    .weight(1f)
                    .semantics { heading() }
            )
        }
        Text(
            text = "Ya no verá cómo estás ni recibirá tus avisos. " +
                "Puedes volver a invitar a ${member.firstName} con un código nuevo.",
            fontFamily = Nunito,
            fontSize = 18.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.Medium,
            color = SereniaPalette.Neutro700
        )
        errorMessage?.let { message ->
            AlertBanner(message = message)
        }
        PrimaryButton(
            text = "Sí, quitar a ${member.firstName}",
            onClick = onConfirm,
            isLoading = isRemoving
        )
        SecondaryButton(
            text = "No, mantener a ${member.firstName}",
            onClick = onCancel,
            enabled = !isRemoving,
            color = SereniaPalette.Durazno800
        )
    }
}

enum class MemberTone(val background: Color, val content: Color) {
    DURAZNO(SereniaPalette.Durazno200, SereniaPalette.Durazno900),
    VERDE(SereniaPalette.Verde200, SereniaPalette.Verde900);

    companion object {
        fun forPosition(position: Int): MemberTone = entries[position % entries.size]
    }
}

@Composable
fun MemberAvatar(
    initials: String,
    tone: MemberTone,
    size: Dp,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 22.sp,
    borderWidth: Dp = 0.dp
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .then(
                if (borderWidth > 0.dp) {
                    Modifier.border(BorderStroke(borderWidth, SereniaPalette.Neutro50), CircleShape)
                } else {
                    Modifier
                }
            )
            .background(tone.background, CircleShape)
    ) {
        Text(
            text = initials,
            fontFamily = Nunito,
            fontSize = fontSize,
            fontWeight = FontWeight.ExtraBold,
            color = tone.content
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun CircleCardsPreview() {
    val lucia = FamilyMember(familyLinkId = "1", relativeId = "1", fullName = "Lucía Mendoza", relationshipLabel = "hija")
    val martin = FamilyMember(familyLinkId = "2", relativeId = "2", fullName = "Martín Ríos", relationshipLabel = "nieto")

    SereniaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            FamilyMemberCard(member = martin, tone = MemberTone.VERDE, onRemove = {})
            RemoveMemberCard(
                member = lucia,
                tone = MemberTone.DURAZNO,
                isRemoving = false,
                errorMessage = null,
                onConfirm = {},
                onCancel = {}
            )
        }
    }
}
