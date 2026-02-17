package com.thefallendeveloper.indianmetro.features.onboarding

sealed interface OnboardingNavigationRoutes {
    data object PassengerDetails : OnboardingNavigationRoutes
}
