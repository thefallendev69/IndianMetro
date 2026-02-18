package com.thefallendeveloper.indianmetro.corecommon.network

actual fun Throwable.isNoNetworkError(): Boolean {
    val message = this.message ?: return false
    return message.contains("timed out", ignoreCase = true) ||
        message.contains("offline", ignoreCase = true) ||
        message.contains("host", ignoreCase = true)
}
