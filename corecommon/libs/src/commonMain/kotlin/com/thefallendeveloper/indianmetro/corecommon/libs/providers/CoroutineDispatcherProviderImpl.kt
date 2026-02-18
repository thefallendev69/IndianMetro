package com.thefallendeveloper.indianmetro.corecommon.libs.providers

import kotlinx.coroutines.CoroutineDispatcher

expect class CoroutineDispatcherProviderImpl() : ICoroutineDispatchersProvider {
    override val main: CoroutineDispatcher
    override val mainImmediate: CoroutineDispatcher
    override val io: CoroutineDispatcher
    override val default: CoroutineDispatcher
    override val unConfined: CoroutineDispatcher
}
