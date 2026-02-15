package com.thefallendeveloper.indianmetro.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.thefallendeveloper.indianmetro.designsystem.tokens.IndianMetroDarkColors
import com.thefallendeveloper.indianmetro.designsystem.tokens.IndianMetroLightColors
import com.thefallendeveloper.indianmetro.designsystem.tokens.IndianMetroTypography
import com.thefallendeveloper.indianmetro.designsystem.tokens.LocalSpacing
import com.thefallendeveloper.indianmetro.designsystem.tokens.Spacing

@Composable
fun IndianMetroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) IndianMetroDarkColors else IndianMetroLightColors
    val spacing = Spacing()

    CompositionLocalProvider(LocalSpacing provides spacing) {
        MaterialTheme(
            colorScheme = colors,
            typography = IndianMetroTypography,
            content = content,
        )
    }
}

object IndianMetroThemeTokens {
    val spacing: Spacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current
}
