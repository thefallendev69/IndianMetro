package com.thefallendeveloper.indianmetro.features.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroThemeTokens
import com.thefallendeveloper.indianmetro.designsystem.tokens.ColorTokens

@Composable
fun AuthScreenPreamble(
    title: String,
    subtitle: String,
) {
    val spacing = IndianMetroThemeTokens.spacing
    Row(modifier = Modifier.fillMaxWidth()) {
        listOf(
            ColorTokens.MetroLines.red,
            ColorTokens.MetroLines.yellow,
            ColorTokens.MetroLines.blue,
            ColorTokens.MetroLines.green,
        ).forEach { color ->
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .background(color)
                        .padding(vertical = 2.dp),
            )
        }
    }

    Box(
        modifier =
            Modifier
                .padding(top = 12.dp, bottom = spacing.small)
                .clip(CircleShape)
                .background(ColorTokens.Brand.primaryEnd.copy(alpha = 0.1f))
                .padding(12.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = ColorTokens.Brand.primaryEnd,
        )
    }

    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge,
    )
    Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp),
    )
}
