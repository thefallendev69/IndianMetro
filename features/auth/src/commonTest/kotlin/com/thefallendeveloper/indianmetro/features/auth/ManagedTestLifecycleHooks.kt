package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.corecommon.basetest.ManagedTestLifecycle
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

interface ManagedTestLifecycleHooks : ManagedTestLifecycle {
    @BeforeTest
    fun setUpManagedTestLifecycle() {
        beforeManagedTestLifecycle()
    }

    @AfterTest
    fun tearDownManagedTestLifecycle() {
        afterManagedTestLifecycle()
    }
}
