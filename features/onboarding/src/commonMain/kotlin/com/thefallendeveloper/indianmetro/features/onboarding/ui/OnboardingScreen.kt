package com.thefallendeveloper.indianmetro.features.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.thefallendeveloper.indianmetro.designsystem.components.GradientPrimaryButton
import com.thefallendeveloper.indianmetro.designsystem.components.MetroLabeledInputField
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroTheme
import com.thefallendeveloper.indianmetro.designsystem.theme.IndianMetroThemeTokens
import com.thefallendeveloper.indianmetro.designsystem.tokens.ColorTokens
import indianmetro.features.onboarding.generated.resources.Res
import indianmetro.features.onboarding.generated.resources.onboarding_create_account
import indianmetro.features.onboarding.generated.resources.onboarding_email_label
import indianmetro.features.onboarding.generated.resources.onboarding_email_placeholder
import indianmetro.features.onboarding.generated.resources.onboarding_first_name_label
import indianmetro.features.onboarding.generated.resources.onboarding_first_name_placeholder
import indianmetro.features.onboarding.generated.resources.onboarding_last_name_label
import indianmetro.features.onboarding.generated.resources.onboarding_last_name_placeholder
import indianmetro.features.onboarding.generated.resources.onboarding_subtitle
import indianmetro.features.onboarding.generated.resources.onboarding_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingScreen(
    state: OnboardingUiState,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onCreateAccount: () -> Unit,
) {
    IndianMetroTheme {
        val spacing = IndianMetroThemeTokens.spacing
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .safeDrawingPadding(),
        ) {
            TopProgressStrip()

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(horizontal = spacing.medium, vertical = spacing.small),
            ) {
                ProfileIcon()
                HeaderSection()

                Row(horizontalArrangement = Arrangement.spacedBy(spacing.small)) {
                    MetroLabeledInputField(
                        label = stringResource(Res.string.onboarding_first_name_label),
                        value = state.firstName,
                        onValueChange = onFirstNameChanged,
                        placeholder = stringResource(Res.string.onboarding_first_name_placeholder),
                        modifier = Modifier.weight(1f),
                    )
                    MetroLabeledInputField(
                        label = stringResource(Res.string.onboarding_last_name_label),
                        value = state.lastName,
                        onValueChange = onLastNameChanged,
                        placeholder = stringResource(Res.string.onboarding_last_name_placeholder),
                        modifier = Modifier.weight(1f),
                    )
                }

                MetroLabeledInputField(
                    label = stringResource(Res.string.onboarding_email_label),
                    value = state.email,
                    onValueChange = onEmailChanged,
                    placeholder = stringResource(Res.string.onboarding_email_placeholder),
                    modifier = Modifier.padding(top = spacing.small),
                )

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter,
                ) {
                    GradientPrimaryButton(
                        text = stringResource(Res.string.onboarding_create_account),
                        onClick = onCreateAccount,
                        enabled = state.firstName.isNotBlank() && state.lastName.isNotBlank(),
                        modifier = Modifier.padding(bottom = spacing.medium),
                    )
                }
            }
        }
    }
}

@Composable
private fun TopProgressStrip() {
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
}

@Composable
private fun ProfileIcon() {
    Box(
        modifier =
            Modifier
                .padding(top = 10.dp, bottom = 14.dp)
                .clip(CircleShape)
                .background(ColorTokens.Brand.primaryStart.copy(alpha = 0.12f))
                .padding(12.dp),
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = ColorTokens.Brand.primaryStart,
        )
    }
}

@Composable
private fun HeaderSection() {
    Text(
        text = stringResource(Res.string.onboarding_title),
        style = MaterialTheme.typography.headlineLarge,
    )
    Text(
        text = stringResource(Res.string.onboarding_subtitle),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp),
    )
}

data class OnboardingUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
)
