package com.thefallendeveloper.indianmetro.corecommon.basetest

actual object DelegateLifecycleInvoker {
    actual fun beforeEach(testInstance: Any) = Unit

    actual fun afterEach(testInstance: Any) = Unit
}
