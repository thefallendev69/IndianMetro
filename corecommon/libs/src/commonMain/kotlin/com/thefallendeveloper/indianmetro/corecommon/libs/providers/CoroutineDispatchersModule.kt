package com.thefallendeveloper.indianmetro.corecommon.libs.providers

import org.koin.core.module.Module
import org.koin.dsl.module

val coroutineDispatchersModule: Module =
    module {
        single<ICoroutineDispatchersProvider> { CoroutineDispatcherProviderImpl() }
    }
