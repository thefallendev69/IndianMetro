package com.thefallendeveloper.indianmetro.features.auth.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thefallendeveloper.indianmetro.designsystem.components.GradientPrimaryButton
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroTheme
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroThemeTokens
import com.thefallendeveloper.indianmetro.designsystem.tokens.ColorTokens
import com.thefallendeveloper.indianmetro.features.auth.viewmodel.OtpEntryViewModel
import indianmetro.features.auth.generated.resources.Res
import indianmetro.features.auth.generated.resources.auth_otp_code_label
import indianmetro.features.auth.generated.resources.auth_otp_placeholder
import indianmetro.features.auth.generated.resources.auth_resend_otp
import indianmetro.features.auth.generated.resources.auth_verify_otp
import indianmetro.features.auth.generated.resources.auth_verify_otp_subtitle
import indianmetro.features.auth.generated.resources.auth_verify_otp_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun AuthOtpScreen(viewModel: OtpEntryViewModel) {
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
                title = stringResource(Res.string.auth_verify_otp_title),
                subtitle = stringResource(Res.string.auth_verify_otp_subtitle),
            )

            OtpCodeField(
                otp = state.otp,
                onOtpChanged = { value ->
                    viewModel.emitEvent(OtpEntryViewModel.Event.OtpChanged(value))
                },
            )
            ResendOtpButton(
                onResend = {
                    viewModel.emitEvent(OtpEntryViewModel.Event.ResendClicked)
                },
                modifier =
                    Modifier
                        .align(Alignment.End)
                        .padding(top = spacing.small),
            )

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                GradientPrimaryButton(
                    text = stringResource(Res.string.auth_verify_otp),
                    onClick = {
                        viewModel.emitEvent(OtpEntryViewModel.Event.VerifyClicked)
                    },
                    enabled = state.verifyClickable,
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
