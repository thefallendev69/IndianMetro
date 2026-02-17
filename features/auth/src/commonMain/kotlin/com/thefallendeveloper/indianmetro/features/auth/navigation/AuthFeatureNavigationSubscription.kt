package com.thefallendeveloper.indianmetro.features.auth.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import org.koin.compose.koinInject

@Composable
fun AuthFeatureNavigationSubscription(
    navHostController: NavHostController,
    featureNavigator: FeatureNavigator<AuthNavigationRoutes> = koinInject(),
) {
    LaunchedEffect(featureNavigator, navHostController) {
        featureNavigator.destination.collect { route ->
            navHostController.navigate(route.route) {
                launchSingleTop = true
            }
        }
    }
}
