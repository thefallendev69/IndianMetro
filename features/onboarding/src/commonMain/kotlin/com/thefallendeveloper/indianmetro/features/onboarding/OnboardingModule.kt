package com.thefallendeveloper.indianmetro.features.onboarding

import org.koin.core.module.Module
import org.koin.dsl.module

val onboardingModule: Module =
    module {
        single { OnboardingFeatureNavigator() }
    }
