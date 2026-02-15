package com.thefallendeveloper.indianmetro.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroThemeTokens
import com.thefallendeveloper.indianmetro.designsystem.tokens.ColorTokens

@Composable
fun GradientPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val colors = IndianMetroThemeTokens.colors
    val shape = RoundedCornerShape(14.dp)
    val gradient =
        Brush.horizontalGradient(
            colors =
                listOf(
                    ColorTokens.Brand.primaryStart,
                    ColorTokens.Brand.primaryEnd,
                ),
        )
    val disabledGradient =
        Brush.horizontalGradient(
            colors =
                listOf(
                    colors.primary.copy(alpha = 0.5f),
                    colors.primary.copy(alpha = 0.5f),
                ),
        )

    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(shape)
                .background(if (enabled) gradient else disabledGradient)
                .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = Color.White,
        )
    }
}
