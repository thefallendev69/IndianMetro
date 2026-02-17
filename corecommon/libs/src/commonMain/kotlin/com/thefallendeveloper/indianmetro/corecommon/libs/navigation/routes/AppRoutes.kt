package com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes

sealed class AppRoutes {
    data object Auth : AppRoutes()
    data object AppOnboarding : AppRoutes()
}
