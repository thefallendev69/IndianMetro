package com.thefallendeveloper.indianmetro.features.auth

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigatorSubscription
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigator
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigationRoutes
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.qualifier.named

@Composable
fun AuthRoute(
    featureNavigator: FeatureNavigator<AuthNavigationRoutes> = koinInject(qualifier = named<AuthNavigator>()),
) {
    val navController = rememberNavController()
    FeatureNavigatorSubscription(
        navHostController = navController,
        featureNavigator = featureNavigator,
        routeMapper = { route -> route.route },
    )

    NavHost(
        navController = navController,
        startDestination = AuthNavigationRoutes.PhoneEntry.route,
    ) {
        composable(AuthNavigationRoutes.PhoneEntry.route) {
            AuthPhoneEntryScreen()
        }
        composable(route = AuthNavigationRoutes.OtpEntry.route) { backStackEntry ->
            AuthOtpScreen()
        }
    }
}
