package com.thefallendeveloper.indianmetro.designsystem.tokens

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val BrandPrimary = Color(0xFF1A5D1A)
private val BrandPrimaryDark = Color(0xFF74C67A)
private val SurfaceLight = Color(0xFFF7FBF7)
private val SurfaceDark = Color(0xFF101411)
private val OnPrimaryLight = Color(0xFFFFFFFF)
private val OnPrimaryDark = Color(0xFF07210A)
private val OnSurfaceLight = Color(0xFF171D17)
private val OnSurfaceDark = Color(0xFFE0E4DD)

val IndianMetroLightColors =
    lightColorScheme(
        primary = BrandPrimary,
        onPrimary = OnPrimaryLight,
        surface = SurfaceLight,
        onSurface = OnSurfaceLight,
        background = SurfaceLight,
        onBackground = OnSurfaceLight,
    )

val IndianMetroDarkColors =
    darkColorScheme(
        primary = BrandPrimaryDark,
        onPrimary = OnPrimaryDark,
        surface = SurfaceDark,
        onSurface = OnSurfaceDark,
        background = SurfaceDark,
        onBackground = OnSurfaceDark,
    )
