package com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes

sealed class AppRoutes(
    route: String,
) : BaseRoute(route = route) {
    data object Auth : AppRoutes(route = "auth")

    data object AppOnboarding : AppRoutes(route = "onboarding")

    data object Done : AppRoutes(route = "done")
}
