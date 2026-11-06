package com.vitalcare.serenia.core.designsystems.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
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
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun DateInputDialog(
    title: String,
    initialYear: Int?,
    initialMonth: Int?,
    initialDay: Int?,
    onConfirm: (year: Int, month: Int, day: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val spanishConfiguration = remember(context) {
        Configuration(context.resources.configuration).apply { setLocale(SPANISH) }
    }
    val spanishContext = remember(context) { context.createConfigurationContext(spanishConfiguration) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        CompositionLocalProvider(
            LocalContext provides spanishContext,
            LocalConfiguration provides spanishConfiguration
        ) {
            DateInputContent(
                title = title,
                initialYear = initialYear,
                initialMonth = initialMonth,
                initialDay = initialDay,
                onConfirm = onConfirm,
                onDismiss = onDismiss
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateInputContent(
    title: String,
    initialYear: Int?,
    initialMonth: Int?,
    initialDay: Int?,
    onConfirm: (year: Int, month: Int, day: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val today = remember { Calendar.getInstance() }
    val todayUtcMillis = remember {
        utcMillis(today.get(Calendar.YEAR), today.get(Calendar.MONTH) + 1, today.get(Calendar.DAY_OF_MONTH))
    }
    val state = rememberDatePickerState(
        initialSelectedDateMillis = if (initialYear != null && initialMonth != null && initialDay != null) {
            utcMillis(initialYear, initialMonth, initialDay)
        } else {
            null
        },
        yearRange = FIRST_YEAR..today.get(Calendar.YEAR),
        initialDisplayMode = DisplayMode.Input,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean = utcTimeMillis <= todayUtcMillis
        }
    )

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

            DatePicker(
                state = state,
                title = null,
                headline = null,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(containerColor = SereniaPalette.Neutro50)
            )

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
                    onClick = {
                        state.selectedDateMillis?.let { millis ->
                            val calendar = Calendar.getInstance(UTC).apply { timeInMillis = millis }
                            onConfirm(
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH) + 1,
                                calendar.get(Calendar.DAY_OF_MONTH)
                            )
                        }
                    },
                    enabled = state.selectedDateMillis != null,
                    elevated = false,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private fun utcMillis(year: Int, month: Int, day: Int): Long {
    return Calendar.getInstance(UTC).apply {
        clear()
        set(year, month - 1, day)
    }.timeInMillis
}

private const val FIRST_YEAR = 1900
private val UTC = TimeZone.getTimeZone("UTC")
private val SPANISH = Locale("es", "PE")

@Preview
@Composable
fun DateInputDialogPreview() {
    SereniaTheme {
        DateInputDialog(
            title = "¿Cuándo naciste?",
            initialYear = 1948,
            initialMonth = 5,
            initialDay = 12,
            onConfirm = { _, _, _ -> },
            onDismiss = {}
        )
    }
}
