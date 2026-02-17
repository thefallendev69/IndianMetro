package com.thefallendeveloper.indianmetro.corecommon.libs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController

@Composable
fun <Route> FeatureNavigatorSubscription(
    navHostController: NavHostController,
    featureNavigator: FeatureNavigator<Route>,
    routeMapper: (Route) -> String,
) {
    LaunchedEffect(featureNavigator, navHostController) {
        featureNavigator.destination.collect { route ->
            navHostController.navigate(routeMapper(route)) {
                launchSingleTop = true
            }
        }
    }
}
