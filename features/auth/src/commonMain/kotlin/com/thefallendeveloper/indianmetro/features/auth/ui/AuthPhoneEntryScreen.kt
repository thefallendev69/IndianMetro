package com.thefallendeveloper.indianmetro.features.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thefallendeveloper.indianmetro.designsystem.components.GradientPrimaryButton
import com.thefallendeveloper.indianmetro.designsystem.components.MetroLabeledPhoneInputField
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroTheme
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroThemeTokens
import com.thefallendeveloper.indianmetro.features.auth.viewmodel.PhoneEntryViewModel
import indianmetro.features.auth.generated.resources.Res
import indianmetro.features.auth.generated.resources.auth_mobile_number_label
import indianmetro.features.auth.generated.resources.auth_phone_placeholder
import indianmetro.features.auth.generated.resources.auth_send_otp
import indianmetro.features.auth.generated.resources.auth_sign_in_subtitle
import indianmetro.features.auth.generated.resources.auth_sign_in_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthPhoneEntryScreen(viewModel: PhoneEntryViewModel = koinViewModel(key = "phone-entry-view-model"),) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    IndianMetroTheme {
        val spacing = IndianMetroThemeTokens.spacing
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .safeDrawingPadding()
                    .padding(spacing.medium),
        ) {
            AuthScreenPreamble(
                title = stringResource(Res.string.auth_sign_in_title),
                subtitle = stringResource(Res.string.auth_sign_in_subtitle),
            )

            MetroLabeledPhoneInputField(
                label = stringResource(Res.string.auth_mobile_number_label),
                value = state.phoneNumber,
                onValueChange = { value ->
                    viewModel.emitEvent(PhoneEntryViewModel.Event.PhoneNumberChanged(value))
                },
                placeholder = stringResource(Res.string.auth_phone_placeholder),
                modifier = Modifier.padding(top = spacing.small),
            )

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                GradientPrimaryButton(
                    text = stringResource(Res.string.auth_send_otp),
                    onClick = {
                        viewModel.emitEvent(PhoneEntryViewModel.Event.ContinueClicked)
                    },
                    enabled = state.continueClickable,
                    modifier = Modifier.padding(bottom = spacing.medium),
                )
            }
        }
    }
}
