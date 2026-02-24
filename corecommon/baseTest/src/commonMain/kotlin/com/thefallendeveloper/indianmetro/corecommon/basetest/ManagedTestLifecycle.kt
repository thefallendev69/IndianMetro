package com.thefallendeveloper.indianmetro.corecommon.basetest

interface ManagedTestLifecycle {
    fun beforeManagedTestLifecycle() {
        DelegateLifecycleInvoker.beforeEach(this)
    }

    fun afterManagedTestLifecycle() {
        DelegateLifecycleInvoker.afterEach(this)
    }
}
