package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.featureNavigatorModule
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.AppRoutes
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigationRoutes
import org.koin.core.module.Module
import org.koin.dsl.module

val authModule: Module =
    module {
        includes(featureNavigatorModule<AuthNavigationRoutes>())
        includes(featureNavigatorModule<AppRoutes>())
        factory { PhoneEntryViewModel(featureNavigator = get()) }
        factory { OtpEntryViewModel(appNavigator = get()) }
    }
