package com.thefallendeveloper.indianmetro.corecommon.basetest

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
