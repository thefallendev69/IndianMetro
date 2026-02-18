package com.thefallendeveloper.indianmetro.corecommon.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.io.IOException
import kotlinx.serialization.json.Json

class KtorNetworkClient(
    private val httpClient: HttpClient = defaultHttpClient(),
) {
    suspend fun get(url: String): NetworkResult<String> =
        try {
            val response = httpClient.get(url)
            val statusCode = response.status.value
            val body = response.body<String>()
            if (statusCode in HTTP_SUCCESS_RANGE) {
                NetworkResult.Success(data = body)
            } else {
                NetworkResult.HttpError(statusCode = statusCode, body = body)
            }
        } catch (throwable: IOException) {
            if (throwable.isNoNetworkError()) {
                NetworkResult.NoNetwork(throwable = throwable)
            } else {
                NetworkResult.Unknown(throwable = throwable)
            }
        } catch (throwable: IllegalStateException) {
            NetworkResult.Unknown(throwable = throwable)
        } catch (throwable: IllegalArgumentException) {
            NetworkResult.Unknown(throwable = throwable)
        }

    companion object {
        fun defaultHttpClient(): HttpClient =
            HttpClient {
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                            isLenient = true
                        },
                    )
                }
            }

        private val HTTP_SUCCESS_RANGE = 200..299
    }
}
