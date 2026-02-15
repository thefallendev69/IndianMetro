package com.thefallendeveloper.indianmetro.designsystem.tokens

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

private fun fontWeightOf(value: Int): FontWeight = FontWeight(value)

val IndianMetroTypography =
    Typography(
        headlineLarge =
            TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = fontWeightOf(TypographyTokenValues.h1FontWeight),
                fontSize = TypographyTokenValues.h1FontSizePx.sp,
                lineHeight = 36.sp,
                letterSpacing = TypographyTokenValues.h1LetterSpacingEm.em,
            ),
        titleLarge =
            TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = fontWeightOf(TypographyTokenValues.h2FontWeight),
                fontSize = TypographyTokenValues.h2FontSizePx.sp,
                lineHeight = 30.sp,
            ),
        titleMedium =
            TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = fontWeightOf(TypographyTokenValues.h3FontWeight),
                fontSize = TypographyTokenValues.h3FontSizePx.sp,
                lineHeight = 26.sp,
            ),
        bodyLarge =
            TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = fontWeightOf(TypographyTokenValues.bodyFontWeight),
                fontSize = TypographyTokenValues.bodyFontSizePx.sp,
                lineHeight = 24.sp,
            ),
        labelLarge =
            TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = fontWeightOf(TypographyTokenValues.labelFontWeight),
                fontSize = TypographyTokenValues.labelFontSizePx.sp,
                lineHeight = 16.sp,
                letterSpacing = TypographyTokenValues.labelLetterSpacingEm.em,
            ),
    )
