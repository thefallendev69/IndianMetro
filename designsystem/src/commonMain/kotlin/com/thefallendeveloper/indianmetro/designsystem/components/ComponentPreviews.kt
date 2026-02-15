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
            PrimaryButton(text = "Continue", onClick = {})
            SecondaryButton(text = "Skip", onClick = {})
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
                label = "Email",
                placeholder = "you@example.com",
            )
            PasswordInputField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "••••••••",
            )
        }
    }
}
