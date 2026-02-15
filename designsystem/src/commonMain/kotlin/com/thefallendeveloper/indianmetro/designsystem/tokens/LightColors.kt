package com.thefallendeveloper.indianmetro.designsystem.tokens

class LightColors : IndianMetroColors() {
    override val primary = ColorTokens.Brand.primaryStart
    override val onPrimary = ColorTokens.Neutral.white
    override val surface = ColorTokens.Semantic.Light.surface
    override val onSurface = ColorTokens.Semantic.Light.textPrimary
    override val background = ColorTokens.Semantic.Light.background
    override val onBackground = ColorTokens.Semantic.Light.textPrimary
}
