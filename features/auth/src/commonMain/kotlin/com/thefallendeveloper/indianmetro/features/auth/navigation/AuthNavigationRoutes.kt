package com.thefallendeveloper.indianmetro.features.auth.navigation

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.json.Json

sealed class AuthNavigationRoutes(
    val route: String,
) {
    data object PhoneEntry : AuthNavigationRoutes(route = "phoneEntry")

    data class OtpEntry(
        val args: OtpEntryArgs,
    ) : AuthNavigationRoutes(route = "otpEntry/${encodeArgs(args)}")

    companion object {
        const val OTP_ENTRY_ARGUMENT = "otpArgs"
        const val OTP_ENTRY_ROUTE_PATTERN = "otpEntry/{$OTP_ENTRY_ARGUMENT}"

        @OptIn(ExperimentalEncodingApi::class)
        private fun encodeArgs(args: OtpEntryArgs): String {
            val json = Json.encodeToString(OtpEntryArgs.serializer(), args)
            return Base64.UrlSafe.encode(json.encodeToByteArray())
        }

        @OptIn(ExperimentalEncodingApi::class)
        fun decodeArgs(encodedArgs: String): OtpEntryArgs {
            val json = Base64.UrlSafe.decode(encodedArgs).decodeToString()
            return Json.decodeFromString(OtpEntryArgs.serializer(), json)
        }
    }
}
