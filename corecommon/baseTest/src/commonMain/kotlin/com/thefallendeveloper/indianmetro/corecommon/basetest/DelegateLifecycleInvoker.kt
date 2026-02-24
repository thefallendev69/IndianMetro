package com.thefallendeveloper.indianmetro.corecommon.basetest

expect object DelegateLifecycleInvoker {
    fun beforeEach(testInstance: Any)

    fun afterEach(testInstance: Any)
}
