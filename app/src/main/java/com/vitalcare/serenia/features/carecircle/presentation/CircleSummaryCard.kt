package com.vitalcare.serenia.features.carecircle.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.chevronRight
import com.vitalcare.serenia.core.designsystems.groups
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.format.joinNames

@Composable
fun CircleSummaryCard(
    members: List<FamilyMember>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(SereniaRadius.lg)
    val shownMembers = members.take(MAX_AVATARS)

    Row(
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 88.dp)
            .shadow(
                elevation = 1.dp,
                shape = shape,
                ambientColor = SereniaPalette.Teal700.copy(alpha = 0.08f),
                spotColor = SereniaPalette.Teal700.copy(alpha = 0.08f)
            )
            .clip(shape)
            .background(SereniaPalette.Neutro50, shape)
            .clickable(role = Role.Button, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        if (shownMembers.isEmpty()) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(52.dp)
                    .background(SereniaPalette.Teal100, CircleShape)
            ) {
                Icon(
                    imageVector = groups,
                    contentDescription = null,
                    tint = SereniaPalette.Teal800,
                    modifier = Modifier.size(30.dp)
                )
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy((-14).dp)) {
                shownMembers.forEachIndexed { position, member ->
                    MemberAvatar(
                        initials = member.initials,
                        tone = MemberTone.forPosition(position),
                        size = 52.dp,
                        fontSize = 18.sp,
                        borderWidth = 3.dp
                    )
                }
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (members.isEmpty()) "Invita a tu familia" else joinNames(members.map { it.firstName }),
                fontFamily = Nunito,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SereniaPalette.Neutro800
            )
            Text(
                text = "Tu código y tus familiares",
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
            tint = SereniaPalette.Teal700,
            modifier = Modifier.size(32.dp)
        )
    }
}

private const val MAX_AVATARS = 3

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun CircleSummaryCardPreview() {
    SereniaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(20.dp)
        ) {
            CircleSummaryCard(
                members = listOf(
                    FamilyMember("1", "1", "Lucía Mendoza", "hija"),
                    FamilyMember("2", "2", "Martín Ríos", "nieto")
                ),
                onClick = {}
            )
            CircleSummaryCard(members = emptyList(), onClick = {})
        }
    }
}
