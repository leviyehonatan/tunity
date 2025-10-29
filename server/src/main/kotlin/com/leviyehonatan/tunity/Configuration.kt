package com.leviyehonatan.tunity

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureCors() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization) // If you use Authorization headers
        allowHeader(HttpHeaders.ContentType) // If you send Content-Type headers
        allowCredentials = true // If you need to send cookies or authentication headers
        anyHost() // This allows all origins, for development purposes.

    }
}
