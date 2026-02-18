package com.thefallendeveloper.indianmetro.corecommon.libs.providers

import kotlinx.coroutines.CoroutineDispatcher

actual interface ICoroutineDispatchersProvider {
    actual val main: CoroutineDispatcher
    actual val mainImmediate: CoroutineDispatcher
    actual val io: CoroutineDispatcher
    actual val default: CoroutineDispatcher
    actual val unConfined: CoroutineDispatcher
}
