package com.thefallendeveloper.indianmetro.features.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collectLatest
import org.koin.compose.koinInject

@Composable
fun AuthFeatureNavigationSubscription(
    viewModel: PhoneEntryViewModel,
    onAuthCompleted: (String) -> Unit,
    featureNavigator: AuthFeatureNavigator = koinInject(),
) {
    LaunchedEffect(viewModel, featureNavigator) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                PhoneEntryViewModel.Effect.NavigateToOtp -> {
                    featureNavigator.push(AuthNavigationRoutes.OtpEntry)
                }

                is PhoneEntryViewModel.Effect.NavigateToOnboarding -> {
                    onAuthCompleted(effect.phoneNumber)
                }
            }
        }
    }
}
