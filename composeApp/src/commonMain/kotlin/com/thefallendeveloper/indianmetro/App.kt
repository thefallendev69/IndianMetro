package com.thefallendeveloper.indianmetro

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thefallendeveloper.indianmetro.designsystem.components.GradientPrimaryButton
import com.thefallendeveloper.indianmetro.designsystem.components.SecondaryButton
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroTheme
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroThemeTokens
import com.thefallendeveloper.indianmetro.features.auth.AuthRoute
import com.thefallendeveloper.indianmetro.features.auth.authModule
import com.thefallendeveloper.indianmetro.features.onboarding.OnboardingScreen
import com.thefallendeveloper.indianmetro.features.onboarding.OnboardingUiState
import com.thefallendeveloper.indianmetro.features.onboarding.onboardingModule
import indianmetro.composeapp.generated.resources.Res
import indianmetro.composeapp.generated.resources.app_account_created
import indianmetro.composeapp.generated.resources.app_continue
import indianmetro.composeapp.generated.resources.app_start_over
import indianmetro.composeapp.generated.resources.app_welcome_user
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication

private object AppNavRoute {
    const val AUTH = "auth"
    const val ONBOARDING = "onboarding"
    const val DONE = "done"
}

@Composable
@Preview
fun App() {
    KoinApplication(application = { modules(authModule, onboardingModule) }) {
        val navController = rememberNavController()
        var onboardingState by remember { mutableStateOf(OnboardingUiState()) }

        NavHost(
            navController = navController,
            startDestination = AppNavRoute.AUTH,
        ) {
            composable(AppNavRoute.AUTH) {
                AuthRoute(
                    onAuthCompleted = { enteredPhone ->
                        navController.navigate(AppNavRoute.ONBOARDING)
                    },
                )
            }

            composable(AppNavRoute.ONBOARDING) {
                OnboardingScreen(
                    state = onboardingState,
                    onFirstNameChanged = { onboardingState = onboardingState.copy(firstName = it) },
                    onLastNameChanged = { onboardingState = onboardingState.copy(lastName = it) },
                    onEmailChanged = { onboardingState = onboardingState.copy(email = it) },
                    onCreateAccount = { navController.navigate(AppNavRoute.DONE) },
                )
            }

            composable(AppNavRoute.DONE) {
                SuccessScreen(
                    fullName = "${onboardingState.firstName} ${onboardingState.lastName}".trim(),
                    onRestart = {
                        onboardingState = OnboardingUiState()
                        navController.navigate(AppNavRoute.AUTH) {
                            popUpTo(navController.graph.id) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun SuccessScreen(
    fullName: String,
    onRestart: () -> Unit,
) {
    IndianMetroTheme {
        val spacing = IndianMetroThemeTokens.spacing
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .safeDrawingPadding()
                    .padding(spacing.large),
            verticalArrangement = Arrangement.spacedBy(spacing.medium),
        ) {
            Text(
                text = stringResource(Res.string.app_account_created),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = stringResource(Res.string.app_welcome_user, fullName),
                style = MaterialTheme.typography.bodyLarge,
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(spacing.small)) {
                    GradientPrimaryButton(
                        text = stringResource(Res.string.app_continue),
                        onClick = {},
                    )
                    SecondaryButton(
                        text = stringResource(Res.string.app_start_over),
                        onClick = onRestart,
                    )
                }
            }
        }
    }
}
