package com.thefallendeveloper.indianmetro.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thefallendeveloper.indianmetro.designsystem.components.GradientPrimaryButton
import com.thefallendeveloper.indianmetro.designsystem.components.MetroLabeledPhoneInputField
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroTheme
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroThemeTokens
import com.thefallendeveloper.indianmetro.designsystem.tokens.ColorTokens
import indianmetro.features.auth.generated.resources.Res
import indianmetro.features.auth.generated.resources.auth_mobile_number_label
import indianmetro.features.auth.generated.resources.auth_otp_code_label
import indianmetro.features.auth.generated.resources.auth_otp_placeholder
import indianmetro.features.auth.generated.resources.auth_phone_placeholder
import indianmetro.features.auth.generated.resources.auth_resend_otp
import indianmetro.features.auth.generated.resources.auth_send_otp
import indianmetro.features.auth.generated.resources.auth_sign_in_subtitle
import indianmetro.features.auth.generated.resources.auth_sign_in_title
import indianmetro.features.auth.generated.resources.auth_verify_otp
import indianmetro.features.auth.generated.resources.auth_verify_otp_subtitle
import indianmetro.features.auth.generated.resources.auth_verify_otp_title
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthRoute(
    onAuthCompleted: (String) -> Unit,
    viewModelKey: String = "auth-view-model",
    viewModel: PhoneEntryViewModel = koinViewModel(key = viewModelKey),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is PhoneEntryViewModel.Effect.NavigateToOnboarding -> onAuthCompleted(effect.phoneNumber)
            }
        }
    }

    when (state.authStep) {
        PhoneEntryViewModel.AuthStep.PhoneEntry ->
            AuthPhoneEntryScreen(
                phoneNumber = state.phoneNumber,
                onPhoneNumberChanged = { value ->
                    viewModel.emitEvent(PhoneEntryViewModel.Event.PhoneNumberChanged(value))
                },
                onContinue = {
                    viewModel.emitEvent(PhoneEntryViewModel.Event.ContinueClicked)
                },
            )

        PhoneEntryViewModel.AuthStep.OtpEntry ->
            AuthOtpScreen(
                otp = state.otp,
                onOtpChanged = { value ->
                    viewModel.emitEvent(PhoneEntryViewModel.Event.OtpChanged(value))
                },
                onVerify = {
                    viewModel.emitEvent(PhoneEntryViewModel.Event.VerifyClicked)
                },
                onResend = {
                    viewModel.emitEvent(PhoneEntryViewModel.Event.ResendClicked)
                },
            )
    }
}

@Composable
fun AuthPhoneEntryScreen(
    phoneNumber: String,
    onPhoneNumberChanged: (String) -> Unit,
    onContinue: () -> Unit,
) {
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
            ScreenPreamble(
                title = stringResource(Res.string.auth_sign_in_title),
                subtitle = stringResource(Res.string.auth_sign_in_subtitle),
            )

            MetroLabeledPhoneInputField(
                label = stringResource(Res.string.auth_mobile_number_label),
                value = phoneNumber,
                onValueChange = onPhoneNumberChanged,
                placeholder = stringResource(Res.string.auth_phone_placeholder),
                modifier = Modifier.padding(top = spacing.small),
            )

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                GradientPrimaryButton(
                    text = stringResource(Res.string.auth_send_otp),
                    onClick = onContinue,
                    enabled = phoneNumber.length >= 10,
                    modifier = Modifier.padding(bottom = spacing.medium),
                )
            }
        }
    }
}

@Composable
fun AuthOtpScreen(
    otp: String,
    onOtpChanged: (String) -> Unit,
    onVerify: () -> Unit,
    onResend: () -> Unit,
) {
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
            ScreenPreamble(
                title = stringResource(Res.string.auth_verify_otp_title),
                subtitle = stringResource(Res.string.auth_verify_otp_subtitle),
            )

            OtpCodeField(otp = otp, onOtpChanged = onOtpChanged)
            ResendOtpButton(
                onResend = onResend,
                modifier =
                    Modifier
                        .align(Alignment.End)
                        .padding(top = spacing.small),
            )

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                GradientPrimaryButton(
                    text = stringResource(Res.string.auth_verify_otp),
                    onClick = onVerify,
                    enabled = otp.length == 6,
                    modifier = Modifier.padding(bottom = spacing.medium),
                )
            }
        }
    }
}

@Composable
private fun OtpCodeField(
    otp: String,
    onOtpChanged: (String) -> Unit,
) {
    Text(
        text = stringResource(Res.string.auth_otp_code_label),
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier.padding(bottom = 6.dp),
    )
    OutlinedTextField(
        value = otp,
        onValueChange = { new ->
            onOtpChanged(new.filter { it.isDigit() }.take(6))
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        placeholder = {
            Text(
                stringResource(Res.string.auth_otp_placeholder),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            ),
    )
}

@Composable
private fun ResendOtpButton(
    onResend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(Res.string.auth_resend_otp),
        color = ColorTokens.Brand.primaryEnd,
        style = MaterialTheme.typography.bodyLarge,
        modifier =
            modifier
                .clip(RoundedCornerShape(8.dp))
                .background(ColorTokens.Brand.primaryEnd.copy(alpha = 0.1f))
                .clickable(onClick = onResend)
                .padding(horizontal = 12.dp, vertical = 8.dp),
    )
}

@Composable
private fun ScreenPreamble(
    title: String,
    subtitle: String,
) {
    val spacing = IndianMetroThemeTokens.spacing
    Row(modifier = Modifier.fillMaxWidth()) {
        listOf(
            ColorTokens.MetroLines.red,
            ColorTokens.MetroLines.yellow,
            ColorTokens.MetroLines.blue,
            ColorTokens.MetroLines.green,
        ).forEach { color ->
            Box(
                modifier =
                    Modifier
                        .weight(1f)
                        .background(color)
                        .padding(vertical = 2.dp),
            )
        }
    }

    Box(
        modifier =
            Modifier
                .padding(top = 12.dp, bottom = spacing.small)
                .clip(CircleShape)
                .background(ColorTokens.Brand.primaryEnd.copy(alpha = 0.1f))
                .padding(12.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Lock,
            contentDescription = null,
            tint = ColorTokens.Brand.primaryEnd,
        )
    }

    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge,
    )
    Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp),
    )
}
