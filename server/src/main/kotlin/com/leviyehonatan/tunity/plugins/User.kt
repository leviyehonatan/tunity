package com.leviyehonatan.tunity.plugins

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.generalRoutes() {
    authenticate("auth-jwt") {
        get("/hello") {
            val principal = call.principal<JWTPrincipal>()
            val username = principal!!.payload.getClaim("username").asString()
            val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
            print("Hello, $username! Token is expired at $expiresAt ms.")
            call.respond(HttpStatusCode.OK, "Authenticated")
        }
    }
}
