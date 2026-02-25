package com.thefallendeveloper.indianmetro.features.auth.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.savedstate.read
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigator
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.FeatureNavigatorSubscription
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.decodeArgs
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigationRoutes
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigator
import com.thefallendeveloper.indianmetro.features.auth.navigation.OtpEntryArgs
import com.thefallendeveloper.indianmetro.features.auth.viewmodel.OtpEntryViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

@Composable
fun AuthRoute(
    featureNavigator: FeatureNavigator<AuthNavigationRoutes> = koinInject(qualifier = named<AuthNavigator>()),
) {
    val navController = rememberNavController()
    FeatureNavigatorSubscription(
        navHostController = navController,
        featureNavigator = featureNavigator,
    )

    NavHost(
        navController = navController,
        startDestination = AuthNavigationRoutes.PhoneEntry.route,
    ) {
        composable(AuthNavigationRoutes.PhoneEntry.route) {
            AuthPhoneEntryScreen()
        }

        composable(
            route = AuthNavigationRoutes.OTP_ENTRY_ROUTE_PATTERN,
            arguments =
                listOf(
                    navArgument(AuthNavigationRoutes.OTP_ENTRY_ARGUMENT) {
                        type = NavType.StringType
                    },
                ),
        ) { backStackEntry ->
            val encodedArgs =
                backStackEntry
                    .arguments
                    ?.read { getStringOrNull(AuthNavigationRoutes.OTP_ENTRY_ARGUMENT) }
                    ?: return@composable
            val otpEntryArgs: OtpEntryArgs = decodeArgs(encodedArgs)
            AuthOtpScreen(
                viewModel =
                    koinViewModel(
                        key = "otp-entry-view-model-${otpEntryArgs.phoneNumber}",
                        parameters = {
                            parametersOf(otpEntryArgs.phoneNumber)
                        },
                    ),
            )
        }
    }
}
