package com.vitalcare.serenia.features.carecircle.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.format.formatDayAndMonth
import com.vitalcare.serenia.core.format.formatTime
import com.vitalcare.serenia.features.carecircle.domain.InvitationCode
import java.util.Calendar

@Composable
fun InvitationCodeCard(
    label: String,
    invitationCode: InvitationCode?,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 180.dp)
            .background(SereniaPalette.Teal700, RoundedCornerShape(SereniaRadius.xl))
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        if (invitationCode == null) {
            CircularProgressIndicator(color = SereniaPalette.Neutro50)
            return@Column
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clearAndSetSemantics {
                contentDescription = "$label: ${invitationCode.code.toList().joinToString(" ")}. " +
                    "Se usa una sola vez. ${expirationText(invitationCode)}"
            }
        ) {
            Text(
                text = label,
                fontFamily = Nunito,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Bold,
                color = SereniaPalette.Neutro50
            )
            Text(
                text = invitationCode.code.chunked(4).joinToString(" "),
                fontFamily = Nunito,
                fontSize = 44.sp,
                lineHeight = 52.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.12.em,
                color = SereniaPalette.Neutro50,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Se usa una sola vez",
                fontFamily = Nunito,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium,
                color = SereniaPalette.Neutro50
            )
            Text(
                text = expirationText(invitationCode),
                fontFamily = Nunito,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = SereniaPalette.Teal100,
                textAlign = TextAlign.Center
            )
        }
    }
}

fun expirationText(invitationCode: InvitationCode): String {
    val calendar = Calendar.getInstance().apply { timeInMillis = invitationCode.expiresAtMillis }
    val date = formatDayAndMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
    val time = formatTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
    return "Vence el $date a las $time"
}

fun shareInvitationCode(context: Context, invitationCode: InvitationCode) {
    val text = "Hola, te invito a acompañarme en Serenia. Mi código es ${invitationCode.code}. " +
        "Escríbelo en tu app de Serenia. ${expirationText(invitationCode)}."
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    context.startActivity(Intent.createChooser(intent, "Compartir código"))
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun InvitationCodeCardPreview() {
    SereniaTheme {
        InvitationCodeCard(
            label = "Tu código para invitar",
            invitationCode = InvitationCode(id = "1", code = "K7M4QX2P", expiresAtMillis = 1_778_250_600_000),
            modifier = Modifier.padding(20.dp)
        )
    }
}
