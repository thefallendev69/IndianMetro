package com.thefallendeveloper.indianmetro.corecommon.libs.providers

import kotlinx.coroutines.CoroutineDispatcher

interface ICoroutineDispatchersProvider {
    val main: CoroutineDispatcher
    val mainImmediate: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
    val unConfined: CoroutineDispatcher
}
