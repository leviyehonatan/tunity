package com.leviyehonatan.tunity.plugins

// plugins/Security.kt
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.v1.jdbc.Database

fun Application.authentication(database: Database) {
    install(Authentication) {
        jwt("auth-jwt") {
            realm = ""
            verifier(
                JWT
                    .require(Algorithm.HMAC256("12345"))
                        .withAudience("")
                        .withIssuer("")
                        .build()
            )

            validate {
                if (it.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(it.payload)
                } else {
                    null
                }
            }
        }
    }
}