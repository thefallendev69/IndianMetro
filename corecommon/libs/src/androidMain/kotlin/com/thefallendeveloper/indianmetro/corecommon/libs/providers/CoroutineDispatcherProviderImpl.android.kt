package com.thefallendeveloper.indianmetro.corecommon.libs.providers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class CoroutineDispatcherProviderImpl : ICoroutineDispatchersProvider {
    actual override val main: CoroutineDispatcher
        get() = Dispatchers.Main
    actual override val mainImmediate: CoroutineDispatcher
        get() = Dispatchers.Main.immediate
    actual override val io: CoroutineDispatcher
        get() = Dispatchers.IO
    actual override val default: CoroutineDispatcher
        get() = Dispatchers.Default
    actual override val unConfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
}
