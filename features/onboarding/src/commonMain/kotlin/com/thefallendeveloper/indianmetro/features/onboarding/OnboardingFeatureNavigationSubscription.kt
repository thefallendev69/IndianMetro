package com.thefallendeveloper.indianmetro.features.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.koinInject

@Composable
fun onboardingFeatureNavigationSubscription(
    featureNavigator: OnboardingFeatureNavigator = koinInject(),
): OnboardingNavigationRoutes {
    val route by featureNavigator.navigationRoute.collectAsState()

    LaunchedEffect(featureNavigator) {
        featureNavigator.push(OnboardingNavigationRoutes.PassengerDetails)
    }

    return route
}
