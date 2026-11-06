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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.format.formatLongDate
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
    onDismiss: () -> Unit,
    upcoming: Boolean = false
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
                onDismiss = onDismiss,
                upcoming = upcoming
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
    onDismiss: () -> Unit,
    upcoming: Boolean
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
        yearRange = if (upcoming) {
            today.get(Calendar.YEAR)..today.get(Calendar.YEAR) + UPCOMING_YEARS
        } else {
            FIRST_YEAR..today.get(Calendar.YEAR)
        },
        initialDisplayMode = if (upcoming) DisplayMode.Picker else DisplayMode.Input,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return if (upcoming) utcTimeMillis >= todayUtcMillis else utcTimeMillis <= todayUtcMillis
            }
        }
    )

    Surface(
        shape = RoundedCornerShape(SereniaRadius.xl),
        color = SereniaPalette.Neutro50,
        shadowElevation = 16.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = if (upcoming) 16.dp else 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(if (upcoming) 12.dp else 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 24.dp, horizontal = if (upcoming) 0.dp else 24.dp)
        ) {
            if (upcoming) {
                UpcomingDateHeader(title = title, selectedDateMillis = state.selectedDateMillis)
            } else {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = SereniaPalette.Neutro800,
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics { heading() }
                )
            }

            DatePicker(
                state = state,
                title = null,
                headline = null,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = SereniaPalette.Neutro50,
                    selectedDayContainerColor = SereniaPalette.Teal700,
                    selectedDayContentColor = SereniaPalette.Neutro50,
                    todayDateBorderColor = SereniaPalette.Teal700,
                    todayContentColor = SereniaPalette.Teal700,
                    selectedYearContainerColor = SereniaPalette.Teal700,
                    selectedYearContentColor = SereniaPalette.Neutro50
                )
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = if (upcoming) 24.dp else 0.dp)
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

@Composable
private fun UpcomingDateHeader(title: String, selectedDateMillis: Long?) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(
                text = title,
                fontFamily = Nunito,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SereniaPalette.Neutro600,
                modifier = Modifier.semantics { heading() }
            )
            if (selectedDateMillis != null) {
                Text(
                    text = formatLongDate(localCalendarFor(selectedDateMillis)),
                    fontFamily = Nunito,
                    fontSize = 24.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SereniaPalette.Neutro800
                )
            }
        }
        HorizontalDivider(
            color = SereniaPalette.Neutro200,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

private fun localCalendarFor(utcMillis: Long): Calendar {
    val utc = Calendar.getInstance(UTC).apply { timeInMillis = utcMillis }
    return Calendar.getInstance().apply {
        clear()
        set(utc.get(Calendar.YEAR), utc.get(Calendar.MONTH), utc.get(Calendar.DAY_OF_MONTH))
    }
}

private fun utcMillis(year: Int, month: Int, day: Int): Long {
    return Calendar.getInstance(UTC).apply {
        clear()
        set(year, month - 1, day)
    }.timeInMillis
}

private const val FIRST_YEAR = 1900
private const val UPCOMING_YEARS = 2
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

@Preview
@Composable
fun UpcomingDateInputDialogPreview() {
    SereniaTheme {
        DateInputDialog(
            title = "¿Qué día?",
            initialYear = 2026,
            initialMonth = 11,
            initialDay = 7,
            onConfirm = { _, _, _ -> },
            onDismiss = {},
            upcoming = true
        )
    }
}
