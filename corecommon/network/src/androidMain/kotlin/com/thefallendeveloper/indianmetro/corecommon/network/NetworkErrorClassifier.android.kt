package com.thefallendeveloper.indianmetro.corecommon.network

import java.io.IOException
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.io.IOException as KxIoException

actual fun Throwable.isNoNetworkError(): Boolean {
    var current: Throwable? = this
    while (current != null) {
        if (
            current is UnknownHostException ||
            current is ConnectException ||
            current is SocketTimeoutException ||
            current is NoRouteToHostException ||
            current is IOException ||
            current is KxIoException
        ) {
            return true
        }
        current = current.cause
    }
    return false
}
