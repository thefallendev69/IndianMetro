package com.thefallendeveloper.indianmetro.features.onboarding.di

import com.thefallendeveloper.indianmetro.features.onboarding.navigation.OnboardingFeatureNavigator
import org.koin.core.module.Module
import org.koin.dsl.module

val onboardingModule: Module =
    module {
        single { OnboardingFeatureNavigator() }
    }
