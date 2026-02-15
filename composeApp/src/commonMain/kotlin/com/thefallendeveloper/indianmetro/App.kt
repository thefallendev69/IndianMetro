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
import com.thefallendeveloper.indianmetro.designsystem.components.GradientPrimaryButton
import com.thefallendeveloper.indianmetro.designsystem.components.SecondaryButton
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroTheme
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroThemeTokens
import com.thefallendeveloper.indianmetro.features.auth.AuthOtpScreen
import com.thefallendeveloper.indianmetro.features.auth.AuthPhoneEntryScreen
import com.thefallendeveloper.indianmetro.features.onboarding.OnboardingScreen
import com.thefallendeveloper.indianmetro.features.onboarding.OnboardingUiState
import indianmetro.composeapp.generated.resources.Res
import indianmetro.composeapp.generated.resources.app_account_created
import indianmetro.composeapp.generated.resources.app_continue
import indianmetro.composeapp.generated.resources.app_start_over
import indianmetro.composeapp.generated.resources.app_welcome_user
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private enum class AppStep {
    PhoneEntry,
    OtpVerification,
    PassengerDetails,
    Done,
}

@Composable
@Preview
fun App() {
    var step by remember { mutableStateOf(AppStep.PhoneEntry) }
    var phone by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var onboardingState by remember { mutableStateOf(OnboardingUiState()) }

    when (step) {
        AppStep.PhoneEntry ->
            AuthPhoneEntryScreen(
                phoneNumber = phone,
                onPhoneNumberChanged = { phone = it.filter { ch -> ch.isDigit() }.take(10) },
                onContinue = { step = AppStep.OtpVerification },
            )

        AppStep.OtpVerification ->
            AuthOtpScreen(
                otp = otp,
                onOtpChanged = { otp = it },
                onVerify = { step = AppStep.PassengerDetails },
                onResend = { otp = "" },
            )

        AppStep.PassengerDetails ->
            OnboardingScreen(
                state = onboardingState,
                onFirstNameChanged = { onboardingState = onboardingState.copy(firstName = it) },
                onLastNameChanged = { onboardingState = onboardingState.copy(lastName = it) },
                onEmailChanged = { onboardingState = onboardingState.copy(email = it) },
                onCreateAccount = { step = AppStep.Done },
            )

        AppStep.Done ->
            SuccessScreen(
                fullName = "${onboardingState.firstName} ${onboardingState.lastName}".trim(),
                onRestart = {
                    phone = ""
                    otp = ""
                    onboardingState = OnboardingUiState()
                    step = AppStep.PhoneEntry
                },
            )
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
