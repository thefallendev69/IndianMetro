package com.thefallendeveloper.indianmetro.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroThemeTokens

@Composable
fun MetroLabeledInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
) {
    Column(modifier = modifier) {
        BaseMetroInputField(
            label = label,
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            keyboardOptions = KeyboardOptions.Default,
        )
    }
}

@Composable
fun MetroLabeledPhoneInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
) {
    Column(modifier = modifier) {
        BaseMetroInputField(
            label = label,
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            keyboardOptions =
                KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                ),
        )
    }
}

@Composable
private fun BaseMetroInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
) {
    val colors = IndianMetroThemeTokens.colors
    Text(
        text = label,
        style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(bottom = 6.dp),
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        placeholder = { Text(placeholder) },
        keyboardOptions = keyboardOptions,
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedContainerColor = colors.surface,
                unfocusedContainerColor = colors.surface,
                focusedBorderColor = colors.primary.copy(alpha = 0.35f),
                unfocusedBorderColor = colors.onSurface.copy(alpha = 0.2f),
                focusedTextColor = colors.onSurface,
                unfocusedTextColor = colors.onSurface,
                focusedPlaceholderColor = colors.onSurface.copy(alpha = 0.45f),
                unfocusedPlaceholderColor = colors.onSurface.copy(alpha = 0.45f),
            ),
    )
}
