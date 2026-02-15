package com.thefallendeveloper.indianmetro.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import com.thefallendeveloper.indianmetro.designsystem.tokens.DarkColors
import com.thefallendeveloper.indianmetro.designsystem.tokens.IndianMetroColors
import com.thefallendeveloper.indianmetro.designsystem.tokens.IndianMetroTypography
import com.thefallendeveloper.indianmetro.designsystem.tokens.LightColors
import com.thefallendeveloper.indianmetro.designsystem.tokens.LocalSpacing
import com.thefallendeveloper.indianmetro.designsystem.tokens.Spacing

@Composable
fun IndianMetroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors: IndianMetroColors = if (darkTheme) DarkColors() else LightColors()
    val spacing = Spacing()

    CompositionLocalProvider(
        LocalSpacing provides spacing,
        LocalIndianMetroColors provides colors,
    ) {
        MaterialTheme(
            colorScheme = colors.toMaterialColorScheme(isDark = darkTheme),
            typography = IndianMetroTypography,
            content = content,
        )
    }
}

object IndianMetroThemeTokens {
    val colors: IndianMetroColors
        @Composable
        @ReadOnlyComposable
        get() = LocalIndianMetroColors.current

    val spacing: Spacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current
}

private fun IndianMetroColors.toMaterialColorScheme(isDark: Boolean): ColorScheme =
    when (isDark) {
        true ->
            darkColorScheme(
                primary = primary,
                onPrimary = onPrimary,
                surface = surface,
                onSurface = onSurface,
                background = background,
                onBackground = onBackground,
            )

        else -> {
            lightColorScheme(
                primary = primary,
                onPrimary = onPrimary,
                surface = surface,
                onSurface = onSurface,
                background = background,
                onBackground = onBackground,
            )
        }
    }

val LocalIndianMetroColors = staticCompositionLocalOf<IndianMetroColors> { LightColors() }
