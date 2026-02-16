package com.thefallendeveloper.indianmetro.features.auth

import org.koin.core.module.Module
import org.koin.dsl.module

val authModule: Module =
    module {
        factory { PhoneEntryViewModel() }
    }
