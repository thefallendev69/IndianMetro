package com.thefallendeveloper.indianmetro.corecommon.libs.navigation

import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

inline fun <reified Route> featureNavigatorModule(qualifier: Qualifier? = null): Module =
    module {
        if (qualifier == null) {
            single { FeatureNavigator<Route>() }
        } else {
            single(qualifier) { FeatureNavigator<Route>() }
        }
    }
