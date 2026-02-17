package com.thefallendeveloper.indianmetro.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroTheme
import indianmetro.corecommon.designsystem.generated.resources.Res
import indianmetro.corecommon.designsystem.generated.resources.preview_button_continue
import indianmetro.corecommon.designsystem.generated.resources.preview_button_skip
import indianmetro.corecommon.designsystem.generated.resources.preview_email_label
import indianmetro.corecommon.designsystem.generated.resources.preview_email_placeholder
import indianmetro.corecommon.designsystem.generated.resources.preview_password_label
import indianmetro.corecommon.designsystem.generated.resources.preview_password_placeholder
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Suppress("UnusedPrivateMember")
@Preview
@Composable
fun ButtonsPreview() {
    IndianMetroTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            PrimaryButton(text = stringResource(Res.string.preview_button_continue), onClick = {})
            SecondaryButton(text = stringResource(Res.string.preview_button_skip), onClick = {})
        }
    }
}

@Suppress("UnusedPrivateMember")
@Preview
@Composable
private fun InputFieldsPreview() {
    IndianMetroTheme {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(16.dp),
        ) {
            InputField(
                value = email,
                onValueChange = { email = it },
                label = stringResource(Res.string.preview_email_label),
                placeholder = stringResource(Res.string.preview_email_placeholder),
            )
            PasswordInputField(
                value = password,
                onValueChange = { password = it },
                label = stringResource(Res.string.preview_password_label),
                placeholder = stringResource(Res.string.preview_password_placeholder),
            )
        }
    }
}
