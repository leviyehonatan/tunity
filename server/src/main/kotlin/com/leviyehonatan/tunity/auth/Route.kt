package com.leviyehonatan.tunity.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.leviyehonatan.tunity.LoginRequest
import com.leviyehonatan.tunity.RegisterRequest
import com.leviyehonatan.tunity.plugins.JwtConfig
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes() {
    post("/register") {
        val body = call.receive<RegisterRequest>()
        if (register(body)) {
            call.respond(HttpStatusCode.OK, "User registered successfully")
        } else {
            call.respond(HttpStatusCode.Conflict, "User Exists")
        }
    }

    post("/login") {
        val body = call.receive<LoginRequest>()
        if (login(body.username, body.password)) {
            val jwtConfig: JwtConfig = call.application.property("jwt")
            val jwtToken = JWT.create()
                .withClaim("username", body.username)
                .sign(Algorithm.HMAC256(jwtConfig.secret))
            call.respond(HttpStatusCode.OK, hashMapOf("token" to jwtToken))
        } else {
            call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
        }
    }
}


