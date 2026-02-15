package com.thefallendeveloper.indianmetro.designsystem.tokens

class DarkColors : IndianMetroColors() {
    override val primary = ColorTokens.Brand.primaryEnd
    override val onPrimary = ColorTokens.Neutral.white
    override val surface = ColorTokens.Semantic.Dark.surface
    override val onSurface = ColorTokens.Semantic.Dark.textPrimary
    override val background = ColorTokens.Semantic.Dark.background
    override val onBackground = ColorTokens.Semantic.Dark.textPrimary
}
