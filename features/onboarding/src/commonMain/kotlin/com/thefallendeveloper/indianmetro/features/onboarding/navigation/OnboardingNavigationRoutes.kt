package com.thefallendeveloper.indianmetro.features.onboarding.navigation

sealed interface OnboardingNavigationRoutes {
    data object PassengerDetails : OnboardingNavigationRoutes
}
