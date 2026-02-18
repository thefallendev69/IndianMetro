package com.thefallendeveloper.indianmetro.corecommon.libs.navigation

import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.BaseRoute
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

inline fun <reified Route : BaseRoute> featureNavigatorModule(qualifier: Qualifier? = null): Module =
    module {
        if (qualifier == null) {
            single { FeatureNavigator<Route>() }
        } else {
            single(qualifier) { FeatureNavigator<Route>() }
        }
    }
