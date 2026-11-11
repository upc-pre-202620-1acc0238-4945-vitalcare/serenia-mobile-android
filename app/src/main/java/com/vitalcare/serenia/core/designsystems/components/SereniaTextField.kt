package com.vitalcare.serenia.core.designsystems.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vitalcare.serenia.core.designsystems.visibility
import com.vitalcare.serenia.core.designsystems.visibilityOff
import com.vitalcare.serenia.core.designsystems.theme.Nunito
import com.vitalcare.serenia.core.designsystems.theme.SereniaContrast
import com.vitalcare.serenia.core.designsystems.theme.SereniaPalette
import com.vitalcare.serenia.core.designsystems.theme.SereniaRadius
import com.vitalcare.serenia.core.designsystems.theme.SereniaTheme
import com.vitalcare.serenia.core.designsystems.theme.SereniaTouch

@Composable
fun SereniaTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    error: String? = null,
    isError: Boolean = error != null,
    helper: String? = null,
    prefix: String? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    maxLength: Int? = null,
    labelContent: (@Composable () -> Unit)? = null
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(SereniaRadius.sm)
    val border = if (isError) {
        BorderStroke(3.dp, SereniaPalette.Error)
    } else {
        BorderStroke(SereniaContrast.stroke(2.dp), SereniaContrast.border(SereniaPalette.Neutro500))
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        if (labelContent != null) {
            labelContent()
        } else {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = SereniaPalette.Neutro800
            )
        }

        BasicTextField(
            value = value,
            onValueChange = { text -> onValueChange(if (maxLength != null) text.take(maxLength) else text) },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontFamily = Nunito,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 28.sp,
                color = SereniaPalette.Neutro800
            ),
            cursorBrush = SolidColor(SereniaPalette.Teal700),
            visualTransformation = if (isPassword && !isPasswordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            keyboardOptions = if (isPassword) {
                keyboardOptions.copy(keyboardType = KeyboardType.Password)
            } else {
                keyboardOptions
            },
            keyboardActions = keyboardActions,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = label
                    if (error != null) error(error)
                },
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = SereniaTouch.seniorPrimary)
                        .background(SereniaPalette.Neutro50, shape)
                        .border(border, shape)
                        .padding(start = 18.dp, end = if (isPassword) 8.dp else 18.dp)
                ) {
                    if (prefix != null) {
                        Text(
                            text = prefix,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = SereniaPalette.Neutro700,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = SereniaPalette.Neutro600,
                                // One line so a long hint is never cut by the height of the field
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        innerTextField()
                    }

                    if (isPassword) {
                        IconButton(
                            onClick = { isPasswordVisible = !isPasswordVisible },
                            modifier = Modifier.size(SereniaTouch.min)
                        ) {
                            Icon(
                                imageVector = if (isPasswordVisible) visibilityOff else visibility,
                                contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = SereniaPalette.Neutro700,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }
        )

        if (error != null) {
            FieldError(message = error)
        } else if (helper != null) {
            Text(
                text = helper,
                fontFamily = Nunito,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = SereniaPalette.Neutro700
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F1EA)
@Composable
fun SereniaFormTextFieldPreview() {
    SereniaTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            SereniaTextField(
                label = "Correo o celular",
                value = "rosa@correo",
                onValueChange = {},
                error = "Revisa el correo: parece incompleto."
            )
            SereniaTextField(
                label = "Contraseña",
                value = "",
                onValueChange = {},
                placeholder = "Tu contraseña",
                helper = "Mínimo 8 letras o números.",
                isPassword = true
            )
        }
    }
}
