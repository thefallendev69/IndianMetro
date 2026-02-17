package com.thefallendeveloper.indianmetro.features.auth.navigation

sealed class AuthNavigationRoutes(
    val route: String,
) {
    data object PhoneEntry : AuthNavigationRoutes(route = "auth/phone-entry")

    data class OtpEntry(
        val phoneNumber: String,
    ) : AuthNavigationRoutes(route = "auth/otp-entry/$phoneNumber")

    companion object {
        const val OTP_ENTRY_ARGUMENT_PHONE_NUMBER = "phoneNumber"
        const val OTP_ENTRY_ROUTE_PATTERN = "auth/otp-entry/{$OTP_ENTRY_ARGUMENT_PHONE_NUMBER}"
    }
}
