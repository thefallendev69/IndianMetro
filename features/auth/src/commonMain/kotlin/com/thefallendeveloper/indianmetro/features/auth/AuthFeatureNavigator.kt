package com.thefallendeveloper.indianmetro.features.auth

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthFeatureNavigator {
    private val _navigationRoute = MutableStateFlow<AuthNavigationRoutes>(AuthNavigationRoutes.PhoneEntry)
    val navigationRoute: StateFlow<AuthNavigationRoutes> = _navigationRoute.asStateFlow()

    fun push(route: AuthNavigationRoutes) {
        _navigationRoute.value = route
    }
}
