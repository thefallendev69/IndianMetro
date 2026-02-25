package com.thefallendeveloper.indianmetro.features.onboarding.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnboardingFeatureNavigator {
    private val _navigationRoute =
        MutableStateFlow<OnboardingNavigationRoutes>(OnboardingNavigationRoutes.PassengerDetails)
    val navigationRoute: StateFlow<OnboardingNavigationRoutes> = _navigationRoute.asStateFlow()

    fun push(route: OnboardingNavigationRoutes) {
        _navigationRoute.value = route
    }
}
