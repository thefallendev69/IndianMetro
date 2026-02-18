package com.thefallendeveloper.indianmetro.features.auth.navigation

import kotlinx.serialization.Serializable

@Serializable
data class OtpEntryArgs(
    val phoneNumber: String,
)
