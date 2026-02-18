package com.thefallendeveloper.indianmetro.corecommon.libs.navigation.routes

import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.json.Json

abstract class BaseRoute(
    val route: String,
)

@OptIn(ExperimentalEncodingApi::class)
inline fun <reified T> BaseRoute.encodeArgs(args: T): String {
    val json = Json.encodeToString(args)
    return Base64.UrlSafe.encode(json.encodeToByteArray())
}

@OptIn(ExperimentalEncodingApi::class)
inline fun <reified T> BaseRoute.decodeArgs(encodedArgs: String): T {
    val json = Base64.UrlSafe.decode(encodedArgs).decodeToString()
    return Json.decodeFromString(json)
}
