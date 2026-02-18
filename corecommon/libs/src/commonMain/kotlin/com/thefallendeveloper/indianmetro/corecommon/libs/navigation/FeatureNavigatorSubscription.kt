package com.thefallendeveloper.indianmetro.corecommon.libs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.BaseRoute

@Composable
fun FeatureNavigatorSubscription(
    navHostController: NavHostController,
    featureNavigator: FeatureNavigator<out BaseRoute>,
) {
    LaunchedEffect(featureNavigator, navHostController) {
        featureNavigator.destination.collect { route ->
            navHostController.navigate(route.route) {
                launchSingleTop = true
            }
        }
    }
}
