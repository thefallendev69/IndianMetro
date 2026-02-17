package com.thefallendeveloper.indianmetro.features.auth.navigation

sealed class AuthNavigationRoutes(
    val route: String,
) {
    data object PhoneEntry : AuthNavigationRoutes(route = "phoneEntry")
    data object OtpEntry: AuthNavigationRoutes(route = "otpEntry")
}
