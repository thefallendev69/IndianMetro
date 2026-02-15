package com.thefallendeveloper.indianmetro.features.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroTheme
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroThemeTokens

@Composable
fun AuthScreen() {
    IndianMetroTheme {
        Column(
            modifier = Modifier.padding(IndianMetroThemeTokens.spacing.medium),
        ) {
            Text(
                text = "Auth",
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}
