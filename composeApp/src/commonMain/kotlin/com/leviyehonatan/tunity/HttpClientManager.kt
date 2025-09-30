package com.leviyehonatan.tunity

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json

fun createHttpClient() = HttpClient {
    install(ContentNegotiation) {
        json()
    }

    defaultRequest {
        host = "localhost"
        port = 8080
    }
}