package com.thefallendeveloper.indianmetro.features.auth.navigation

import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.BaseRoute
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.decodeArgs
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.encodeArgs

sealed class AuthNavigationRoutes(
    route: String,
) : BaseRoute(route = route) {
    data object PhoneEntry : AuthNavigationRoutes(route = "phoneEntry")

    data class OtpEntry(
        val args: OtpEntryArgs,
    ) : AuthNavigationRoutes(
            route = "otpEntry/${PhoneEntry.encodeArgs(args)}",
        )

    companion object {
        const val OTP_ENTRY_ARGUMENT = "otpArgs"
        const val OTP_ENTRY_ROUTE_PATTERN = "otpEntry/{$OTP_ENTRY_ARGUMENT}"
    }
}
