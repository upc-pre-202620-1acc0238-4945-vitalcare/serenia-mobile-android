package com.vitalcare.serenia.core.designsystems.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInputDialog(
    title: String,
    initialHour: Int,
    initialMinute: Int,
    onConfirm: (hour: Int, minute: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = false
    )

    val context = LocalContext.current
    val spanishConfiguration = remember(context) {
        Configuration(context.resources.configuration).apply { setLocale(SPANISH) }
    }
    val spanishContext = remember(context) { context.createConfigurationContext(spanishConfiguration) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(SereniaRadius.xl),
            color = SereniaPalette.Neutro50,
            shadowElevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = SereniaPalette.Neutro800,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { heading() }
                )

                CompositionLocalProvider(
                    LocalContext provides spanishContext,
                    LocalConfiguration provides spanishConfiguration
                ) {
                    TimeInput(
                        state = state,
                    colors = TimePickerDefaults.colors(
                        containerColor = SereniaPalette.Neutro50,
                        periodSelectorBorderColor = SereniaPalette.Neutro300,
                        periodSelectorSelectedContainerColor = SereniaPalette.Durazno200,
                        periodSelectorUnselectedContainerColor = SereniaPalette.Neutro50,
                        periodSelectorSelectedContentColor = SereniaPalette.Durazno900,
                        periodSelectorUnselectedContentColor = SereniaPalette.Neutro800,
                        timeSelectorSelectedContainerColor = SereniaPalette.Teal50,
                        timeSelectorUnselectedContainerColor = SereniaPalette.Neutro100,
                        timeSelectorSelectedContentColor = SereniaPalette.Teal800,
                        timeSelectorUnselectedContentColor = SereniaPalette.Neutro800
                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SecondaryButton(
                        text = "Cancelar",
                        onClick = onDismiss,
                        color = SereniaPalette.Durazno700,
                        modifier = Modifier.weight(1f)
                    )
                    PrimaryButton(
                        text = "Listo",
                        onClick = { onConfirm(state.hour, state.minute) },
                        elevated = false,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

private val SPANISH = Locale("es", "PE")

@Preview
@Composable
fun TimeInputDialogPreview() {
    SereniaTheme {
        TimeInputDialog(
            title = "¿A qué hora?",
            initialHour = 10,
            initialMinute = 0,
            onConfirm = { _, _ -> },
            onDismiss = {}
        )
    }
}
