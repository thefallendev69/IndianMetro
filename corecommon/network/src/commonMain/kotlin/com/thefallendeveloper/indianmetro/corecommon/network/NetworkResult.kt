package com.thefallendeveloper.indianmetro.corecommon.network

sealed interface NetworkResult<out Data> {
    data class Success<Data>(
        val data: Data,
    ) : NetworkResult<Data>

    data class HttpError(
        val statusCode: Int,
        val body: String,
    ) : NetworkResult<Nothing>

    data class NoNetwork(
        val throwable: Throwable,
    ) : NetworkResult<Nothing>

    data class Unknown(
        val throwable: Throwable,
    ) : NetworkResult<Nothing>
}
