package com.thefallendeveloper.indianmetro.features.auth

import com.thefallendeveloper.indianmetro.corecommon.libs.providers.ICoroutineDispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher

class TestCoroutineDispatchersProvider(
    private val dispatcher: TestDispatcher,
) : ICoroutineDispatchersProvider {
    override val main: CoroutineDispatcher
        get() = dispatcher
    override val mainImmediate: CoroutineDispatcher
        get() = dispatcher
    override val io: CoroutineDispatcher
        get() = dispatcher
    override val default: CoroutineDispatcher
        get() = dispatcher
    override val unConfined: CoroutineDispatcher
        get() = dispatcher
}
