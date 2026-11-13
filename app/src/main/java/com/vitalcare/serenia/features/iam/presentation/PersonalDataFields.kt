package com.vitalcare.serenia.features.iam.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vitalcare.serenia.core.designsystems.components.ChosenValueCard
import com.vitalcare.serenia.core.designsystems.components.DateInputDialog
import com.vitalcare.serenia.core.designsystems.components.SereniaTextField
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.format.formatDate
import com.vitalcare.serenia.features.iam.domain.BirthDate
import com.vitalcare.serenia.features.iam.domain.CredentialsValidator
import com.vitalcare.serenia.features.iam.domain.PhoneNumber

@Composable
fun PersonalDataFields(
    fullName: String,
    fullNameError: String?,
    onFullNameChange: (String) -> Unit,
    birthDate: BirthDate?,
    onBirthDateChange: (BirthDate) -> Unit,
    phoneNumber: String,
    phoneNumberError: String?,
    onPhoneNumberChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isDatePickerVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        SereniaTextField(
            label = "¿Cómo te llamas?",
            value = fullName,
            onValueChange = onFullNameChange,
            placeholder = "Ej. Rosa Mendoza",
            error = fullNameError,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )

        ChosenValueCard(
            label = "Fecha de nacimiento (opcional)",
            value = birthDate?.let { formatDate(it.year, it.month, it.day) } ?: "Sin elegir",
            actionLabel = if (birthDate == null) "Elegir" else "Cambiar",
            onChange = { isDatePickerVisible = true },
            modifier = Modifier.padding(top = 4.dp)
        )

        SereniaTextField(
            label = "Celular (opcional)",
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            placeholder = "987 654 321",
            error = phoneNumberError,
            prefix = PhoneNumber.COUNTRY_CODE,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next)
        )
    }

    if (isDatePickerVisible) {
        DateInputDialog(
            title = "¿Cuándo naciste?",
            initialYear = birthDate?.year,
            initialMonth = birthDate?.month,
            initialDay = birthDate?.day,
            onConfirm = { year, month, day ->
                isDatePickerVisible = false
                onBirthDateChange(BirthDate(year, month, day))
            },
            onDismiss = { isDatePickerVisible = false }
        )
    }
}

fun fullNameErrorFor(fullName: String): String? {
    return when {
        fullName.isBlank() -> "Escribe tu nombre."
        !CredentialsValidator.isValidName(fullName) -> "Tu nombre es muy largo. Usa uno más corto."
        else -> null
    }
}

fun phoneNumberErrorFor(phoneNumber: String): String? {
    return if (phoneNumber.isEmpty() || PhoneNumber.isValidLocal(phoneNumber)) {
        null
    } else {
        "Revisa el celular: debe tener 9 números y empezar con 9."
    }
}

fun String.toPhoneDigits(): String = filter { it.isDigit() }.take(PhoneNumber.LOCAL_LENGTH)

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun PersonalDataFieldsPreview() {
    SereniaTheme {
        PersonalDataFields(
            fullName = "Rosa Mendoza",
            fullNameError = null,
            onFullNameChange = {},
            birthDate = BirthDate(1948, 5, 12),
            onBirthDateChange = {},
            phoneNumber = "98765",
            phoneNumberError = "Revisa el celular: debe tener 9 números y empezar con 9.",
            onPhoneNumberChange = {},
            modifier = Modifier.padding(24.dp)
        )
    }
}
