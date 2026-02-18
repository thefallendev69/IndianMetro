package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.AppNavigator
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.featureNavigatorModule
import com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes.AppRoutes
import com.thefallendeveloper.indianmetro.corecommon.libs.providers.ICoroutineDispatchersProvider
import com.thefallendeveloper.indianmetro.corecommon.libs.providers.coroutineDispatchersModule
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigationRoutes
import com.thefallendeveloper.indianmetro.features.auth.navigation.AuthNavigator
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

val authModule: Module =
    module {
        includes(coroutineDispatchersModule)
        includes(featureNavigatorModule<AuthNavigationRoutes>(named<AuthNavigator>()))
        includes(featureNavigatorModule<AppRoutes>(named<AppNavigator>()))
        factory {
            val coroutineDispatchersProvider = get<ICoroutineDispatchersProvider>()
            PhoneEntryViewModel(
                featureNavigator = get(named<AuthNavigator>()),
                coroutineDispatchersProvider = coroutineDispatchersProvider,
            )
        }
        factory { (phoneNumber: String) ->
            val coroutineDispatchersProvider = get<ICoroutineDispatchersProvider>()
            OtpEntryViewModel(
                phoneNumber = phoneNumber,
                appNavigator = get(named<AppNavigator>()),
                coroutineDispatchersProvider = coroutineDispatchersProvider,
            )
        }
    }
