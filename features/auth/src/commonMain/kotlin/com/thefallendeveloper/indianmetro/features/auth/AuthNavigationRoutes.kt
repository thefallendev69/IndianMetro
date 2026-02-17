package com.thefallendeveloper.indianmetro.features.auth

sealed class AuthNavigationRoutes(
    val route: String,
) {
    data object PhoneEntry : AuthNavigationRoutes(route = "auth/phone-entry")

    data object OtpEntry : AuthNavigationRoutes(route = "auth/otp-entry")
}
