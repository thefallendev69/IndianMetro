package com.thefallendeveloper.indianmetro.corecommon.libs.navigation

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class FeatureNavigator<Route> {
    private val _destination =
        MutableSharedFlow<Route>(
            replay = 0,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )

    val destination: SharedFlow<Route> = _destination.asSharedFlow()

    fun navigateTo(route: Route) {
        _destination.tryEmit(route)
    }
}
