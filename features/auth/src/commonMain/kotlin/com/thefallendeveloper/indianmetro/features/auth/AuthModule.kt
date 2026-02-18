package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.featureNavigatorModule
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.AppNavigator
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.AppRoutes
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigator
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigationRoutes
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule: Module =
    module {
        includes(featureNavigatorModule<AuthNavigationRoutes>(named<AuthNavigator>()))
        includes(featureNavigatorModule<AppRoutes>(named<AppNavigator>()))
        factory { PhoneEntryViewModel(featureNavigator = get(named<AuthNavigator>())) }
        factory { (phoneNumber: String) ->
            OtpEntryViewModel(
                phoneNumber = phoneNumber,
                appNavigator = get(named<AppNavigator>()),
            )
        }
    }
