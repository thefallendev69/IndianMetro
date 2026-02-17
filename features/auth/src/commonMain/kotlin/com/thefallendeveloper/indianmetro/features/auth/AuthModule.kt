package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.featureNavigatorModule
import org.koin.core.module.Module
import org.koin.dsl.module

val authModule: Module =
    module {
        includes(featureNavigatorModule<AuthNavigationRoutes>())
        factory { PhoneEntryViewModel() }
    }
