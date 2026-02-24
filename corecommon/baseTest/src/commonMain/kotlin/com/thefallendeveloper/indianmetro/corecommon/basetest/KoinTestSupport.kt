package com.thefallendeveloper.indianmetro.corecommon.basetest

import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module

interface KoinSupport {
    fun startKoinForTest(vararg modules: Module)

    fun stopKoinForTest()
}

class KoinTestSupport : KoinSupport {
    override fun startKoinForTest(vararg modules: Module) {
        runCatching { stopKoin() }
        startKoin {
            modules(modules.toList())
        }
    }

    override fun stopKoinForTest() {
        runCatching { stopKoin() }
    }
}
