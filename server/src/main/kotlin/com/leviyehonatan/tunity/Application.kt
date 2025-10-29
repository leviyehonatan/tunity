package com.leviyehonatan.tunity

import com.leviyehonatan.tunity.data.createDatabase
import com.leviyehonatan.tunity.plugins.authentication
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    createDatabase()
    initDb()
    authentication()
    configureCors()
    install(ContentNegotiation) {
        json()
    }
    routes()
}

