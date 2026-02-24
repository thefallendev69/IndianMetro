package com.thefallendeveloper.indianmetro.corecommon.basetest

interface ManagedTestLifecycleHooks : ManagedTestLifecycle {
    fun setUpManagedTestLifecycle() {
        beforeManagedTestLifecycle()
    }

    fun tearDownManagedTestLifecycle() {
        afterManagedTestLifecycle()
    }
}
